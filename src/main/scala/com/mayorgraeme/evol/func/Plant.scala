
package com.mayorgraeme.evol.func

import com.mayorgraeme.evol.func._
import com.mayorgraeme.evol.func.EvolveFunc._
import com.mayorgraeme.evol.util.ArrayFunctions._
import com.mayorgraeme.evol._
import com.mayorgraeme.evol.data.java.PlantData
import com.mayorgraeme.evol.data.java.ActorData
import com.mayorgraeme.evol.enums.LocationType._
import java.util.UUID
import scala.util.Random


case class Plant(maxAge:Int, sproutTime:Int, size:Int, seedRadius:Int, spermRadius: Int, gender: Char, allowedLocationTypes: Set[LocationType], chanceOfPropogation: Int, chanceOfBreeding: Int) extends Inhabitant {
  val uuid = UUID.randomUUID.getMostSignificantBits
  var currentAge = sproutTime
  val rand = new Random()
    
  override def getActorData(): ActorData = {
    new PlantData(uuid, "plant", currentAge, chanceOfPropogation, 0, 0, 0)
  }
  
  def breedPlants(plantOne: Plant, plantTwo: Plant): Seed = {
    val childMaxAge = geneticTransformation(plantOne.maxAge, plantTwo.maxAge)    
    val childChanceOfPropogation = geneticTransformation(plantOne.chanceOfPropogation, plantTwo.chanceOfPropogation)
    val childChanceOfBreeding = geneticTransformation(plantOne.chanceOfBreeding, plantTwo.chanceOfBreeding)
    
    //println("BREED: MaxAge", childMaxAge, "ChanceOfBreeding", childChanceOfBreeding,  "ChanceOfPropagation", childChanceOfPropogation)
    
    new Seed(childMaxAge, sproutTime, size, seedRadius, spermRadius, {if(rand.nextInt(2) == 1) 'M' else 'F' }, allowedLocationTypes, childChanceOfPropogation, childChanceOfBreeding)
  }
  
  def geneticTransformation(first: Int, second: Int): Int = {
    val possibleMin: Int = Math.min(first, second)
    val possibleMax: Int = Math.max(first, second)
    
    val minDelta = possibleMin * 0.1
    val maxDelta = possibleMax * 0.1
    
    val min: Int = Math.floor(possibleMin - minDelta).toInt
    val max: Int = Math.ceil(possibleMax + maxDelta).toInt
    
    rand.nextInt(max-min+1) + min
  }
  
  override def transformWorld(world: World, locationInformation: LocationInformation): World = {
    currentAge = currentAge + 1
      
    //println(currentAge, maxAge)
    if(currentAge >= maxAge) {
      subFromWorld(world, locationInformation.x, locationInformation.y, this)
    } else if (gender == 'M' && percentChance(chanceOfBreeding)){ 
      val locsInRadius = circleMembers[LocationInformation](world, locationInformation.x, locationInformation.y, spermRadius)
      
      val freeSpaces = getFreeSpaces(locsInRadius)
      
      val inhabitants: Seq[Inhabitant] = for{inhabitants <- locsInRadius
                                             inhabitant <- inhabitants.inhabitants
                                             if(inhabitant match {
            case Plant(_,_,_,_,_,'F',_,_,_) => true
            case _ => false                 
          })} yield (inhabitant)
      
      val sexPartners: Seq[Plant] = inhabitants.collect{case plant: Plant if plant.gender == 'F' =>  plant}
          
      if(!sexPartners.isEmpty && !freeSpaces.isEmpty){
        val sexPartner = sexPartners(rand.nextInt(sexPartners.size))
        val child = breedPlants(sexPartner, this)
        
        val freeSpace = freeSpaces(rand.nextInt(freeSpaces.size))
        
        addToWorld(world, freeSpace.x, freeSpace.y, child)
      }else{
        world
      }
    }else if(percentChance(chanceOfPropogation)){
      //println("TREE TIME "+circleMembers[LocationInformation](world, locationInformation.x, locationInformation.y, 1).size)
      val spacesWithoutPlants = getFreeSpaces(circleMembers[LocationInformation](world, locationInformation.x, locationInformation.y, seedRadius))
        
      if(!spacesWithoutPlants.isEmpty){
        val space = spacesWithoutPlants(rand.nextInt(spacesWithoutPlants.size))
        addToWorld(world, space.x, space.y, new Seed(maxAge, sproutTime, size, seedRadius, spermRadius, gender, allowedLocationTypes, chanceOfPropogation, chanceOfBreeding))
      } else {
        world
      }
    } else {
      world
    }
  }
  
  def getFreeSpaces(locationInformation: Seq[LocationInformation]): Seq[LocationInformation] = {
    locationInformation.filter{g => 
      allowedLocationTypes.contains(g.locationType) && !g.equals(locationInformation) && g.inhabitants.forall{ g => 
        g match {
          case Plant(_,_,_,_,_,_,_,_,_) => false
          case Seed(_,_,_,_,_,_,_,_,_) => false
          case _ => true
        }
      }}
  }
  
}
