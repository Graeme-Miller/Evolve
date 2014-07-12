
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
import com.mayorgraeme.evol.util.SexUtil._


case class Plant(maxAge:Int, sproutTime:Int, size:Int, seedRadius:Int, spermRadius: Int, gender: Char, allowedLocationTypes: Set[LocationType], chanceOfPropogation: Int, chanceOfBreeding: Int, waterNeed:Int) extends Inhabitant {
  val uuid = UUID.randomUUID.getMostSignificantBits
  var currentAge = sproutTime
  var currentSize: Double = 1
  val rand = new Random()
  
  val WATER_PERCENTAGE_SUFFER = 20
    
  override def getActorData(): ActorData = {
    new PlantData(uuid, "plant", gender, currentAge, maxAge, sproutTime, size, seedRadius, spermRadius, chanceOfPropogation, chanceOfBreeding, waterNeed)
  }
  
  def breedPlants(plantOne: Plant, plantTwo: Plant): Seed = {
    val childMaxAge = geneticTransformation(plantOne.maxAge, plantTwo.maxAge)    
    val childChanceOfPropogation = geneticTransformation(plantOne.chanceOfPropogation, plantTwo.chanceOfPropogation)
    val childChanceOfBreeding = geneticTransformation(plantOne.chanceOfBreeding, plantTwo.chanceOfBreeding)
    val waterNeed = geneticTransformation(plantOne.waterNeed, plantTwo.waterNeed)
    
    //println("BREED: MaxAge", childMaxAge, "ChanceOfBreeding", childChanceOfBreeding,  "ChanceOfPropagation", childChanceOfPropogation)
    
    new Seed(childMaxAge, sproutTime, size, seedRadius, spermRadius, {if(rand.nextInt(2) == 1) 'M' else 'F' }, allowedLocationTypes, childChanceOfPropogation, childChanceOfBreeding, waterNeed)
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
    def propagate = {
      //println("TREE TIME "+circleMembers[LocationInformation](world, locationInformation.x, locationInformation.y, 1).size)
      val spacesWithoutPlants = getFreeSpaces(circleMembers[LocationInformation](world, locationInformation.x, locationInformation.y, seedRadius))
        
      if(!spacesWithoutPlants.isEmpty){
        val space = spacesWithoutPlants(rand.nextInt(spacesWithoutPlants.size))
        addToWorld(world, space.x, space.y, new Seed(maxAge, sproutTime, size, seedRadius, spermRadius, gender, allowedLocationTypes, chanceOfPropogation, chanceOfBreeding, waterNeed))
      } else {
        world
      }
    }
    
    def haveSex = {
      val locsInRadius = circleMembers[LocationInformation](world, locationInformation.x, locationInformation.y, spermRadius)      
      val freeSpaces = getFreeSpaces(locsInRadius)
         
      val sexPartners: Seq[Plant] = {
        for{inhabitants <- locsInRadius
            inhabitant <- inhabitants.inhabitants                                               
        } yield (inhabitant)}.collect{case plant: Plant if plant.gender == getOpositeSex(gender) =>  plant}
                
      if(!sexPartners.isEmpty && !freeSpaces.isEmpty){
        val sexPartner = sexPartners(rand.nextInt(sexPartners.size))
        val child = breedPlants(sexPartner, this)
        
        val freeSpace = freeSpaces(rand.nextInt(freeSpaces.size))
        
        addToWorld(world, freeSpace.x, freeSpace.y, child)
      }else{
        world
      }
    }    
    
    
    def mature = {
      if(waterNeed <= locationInformation.waterValue) {
        currentSize = currentSize + 1
      }else {
        val waterShortage = waterNeed - locationInformation.waterValue        
        currentSize = currentSize + (waterShortage / WATER_PERCENTAGE_SUFFER)
      }
    }
    
    def killedByWater: Boolean = {      
      if(waterNeed <= locationInformation.waterValue) {
        false
      } else {        
        val waterShortage = waterNeed - locationInformation.waterValue        
        val waterShortageConstrained = Math.min(waterShortage, WATER_PERCENTAGE_SUFFER)        
        val percentChanceOfDeath = ((waterShortageConstrained / WATER_PERCENTAGE_SUFFER) * 100).toInt
        percentChance(percentChanceOfDeath)
      }      
    }
    def shouldDie: Boolean = {
      currentAge >= maxAge  || killedByWater
    }
    
    currentAge = currentAge + 1
      
    //println(currentAge, maxAge)
    if(shouldDie) {
      subFromWorld(world, locationInformation.x, locationInformation.y, this)
    } else if (currentSize < size) { //not yet matured
      mature
      world
    } else if (percentChance(chanceOfBreeding)){ 
      haveSex
    }else if(percentChance(chanceOfPropogation)){
      propagate
    } else {
      world
    }
  }
  
  def getFreeSpaces(locationInformation: Seq[LocationInformation]): Seq[LocationInformation] = {
    locationInformation.filter{g => 
      allowedLocationTypes.contains(g.locationType) && !g.equals(locationInformation) && g.inhabitants.forall{ g => 
        g match {
          case plant: Plant => false
          case seed: Seed => false
          case _ => true
        }
      }}
  }
  
}
