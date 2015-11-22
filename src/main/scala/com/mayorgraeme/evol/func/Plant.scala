
package com.mayorgraeme.evol.func

import com.mayorgraeme.evol.func._
import com.mayorgraeme.evol.func.EvolveFunc._
import com.mayorgraeme.evol.util.ArrayFunctions._
import com.mayorgraeme.evol._
import com.mayorgraeme.evol.data.java.PlantData
import com.mayorgraeme.evol.data.java.ActorData
import com.mayorgraeme.evol.enums.LocationType._
import java.util.UUID
import scala.collection.immutable.Queue
import scala.util.Random
import com.mayorgraeme.evol.util.SexUtil._
import com.mayorgraeme.evol.util.BoundedQueue._
import com.mayorgraeme.evol.util.GeneticUtil._


case class Plant(species: String, maxAge:Int, sproutTime:Int, size:Int, seedRadius:Int, spermRadius: Int, gender: Char, allowedLocationTypes: Set[LocationType], chanceOfPropogation: Int, chanceOfBreeding: Int, waterNeed:Int, parents: Queue[Plant]) extends Inhabitant {
  val uuid = UUID.randomUUID.getMostSignificantBits
  var currentAge = sproutTime
  var currentSize: Double = 1
  val rand = new Random()
  
  val WATER_PERCENTAGE_SUFFER = 20
  val NUM_PARENTS = 50
    
  override def getActorData(): ActorData = {
    new PlantData(uuid, species, "plant", gender, maxAge, currentAge, sproutTime, size, seedRadius, spermRadius, chanceOfPropogation, chanceOfBreeding, waterNeed)
  }
  
  override def hashCode = uuid.toInt
  override def toString = "P"+gender+"-"+uuid
  override def withUpdatedSpecies(newSpecies: String): Inhabitant = new Plant(newSpecies, maxAge, sproutTime, size, seedRadius, spermRadius, gender, allowedLocationTypes, chanceOfPropogation, chanceOfBreeding, waterNeed, parents)

  def similarEnough(plantOne: Plant, plantTwo:Plant): Boolean = {
    geneticSimilarity(plantOne.waterNeed, plantTwo.waterNeed) && 
    geneticSimilarity(plantOne.maxAge, plantTwo.maxAge) && 
    geneticSimilarity(plantOne.chanceOfPropogation, plantTwo.chanceOfPropogation)  && 
    geneticSimilarity(plantOne.chanceOfBreeding, plantTwo.chanceOfBreeding) 
  }
  
  def similarEnough(plant: Plant, seed:Seed): Boolean = {
    geneticSimilarity(plant.waterNeed, seed.waterNeed) && 
    geneticSimilarity(plant.maxAge, seed.maxAge) && 
    geneticSimilarity(plant.chanceOfPropogation, seed.chanceOfPropogation)  && 
    geneticSimilarity(plant.chanceOfBreeding, seed.chanceOfBreeding) 
  }
  
  override def canBreed(other: Inhabitant): Boolean = {
    other match {
      case otherPlant: Plant => this.gender != otherPlant.gender && similarEnough(this, otherPlant) && !this.parents.intersect(otherPlant.parents).isEmpty
      case otherSeed: Seed => this.gender != otherSeed.gender && similarEnough(this, otherSeed) && !this.parents.intersect(otherSeed.parents).isEmpty
      case _ => false  
    }      
  }
  
  def breedPlants(plantOne: Plant, plantTwo: Plant): Seed = {    
    val childChanceOfPropogation = geneticTransformation(plantOne.chanceOfPropogation, plantTwo.chanceOfPropogation)
    val childChanceOfBreeding = geneticTransformation(plantOne.chanceOfBreeding, plantTwo.chanceOfBreeding)
    val waterNeed = geneticTransformation(plantOne.waterNeed, plantTwo.waterNeed)
    val childMaxAge = Math.min(waterNeed, geneticTransformation(plantOne.maxAge, plantTwo.maxAge)) //remove this min, shouldnt need to constrain age
  
    //println("BREED: MaxAge", childMaxAge, "ChanceOfBreeding", childChanceOfBreeding,  "ChanceOfPropagation", childChanceOfPropogation)
   
    val newParents = parents.add(plantOne, NUM_PARENTS).add(plantTwo, NUM_PARENTS)
              
    
    new Seed(species, childMaxAge, sproutTime, size, seedRadius, spermRadius, {if(rand.nextInt(2) == 1) 'M' else 'F' }, allowedLocationTypes, childChanceOfPropogation, childChanceOfBreeding, waterNeed, newParents)
  }
  
  
  
  override def transformWorld(world: World, locationInformation: LocationInformation): World = {
    def propagate = {
      //println("TREE TIME "+circleMembers[LocationInformation](world, locationInformation.x, locationInformation.y, 1).size)
      val spacesWithoutPlants = getFreeSpaces(circleMembers[LocationInformation](world, locationInformation.x, locationInformation.y, seedRadius))
        
      if(!spacesWithoutPlants.isEmpty){
        val space = spacesWithoutPlants(rand.nextInt(spacesWithoutPlants.size))
        addToWorld(world, space.x, space.y, new Seed(species, maxAge, sproutTime, size, seedRadius, spermRadius, gender, allowedLocationTypes, chanceOfPropogation, chanceOfBreeding, waterNeed, parents))
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
        } yield (inhabitant)}.toStream.collect{case plant: Plant if(canBreed(plant)) =>  plant}
                
      if(!sexPartners.isEmpty && !freeSpaces.isEmpty){
        val sexPartner = sexPartners(0)
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
      } else if (locationInformation.waterValue == 0) {
        true 
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
