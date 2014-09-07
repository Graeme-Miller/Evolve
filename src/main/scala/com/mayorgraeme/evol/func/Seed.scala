
package com.mayorgraeme.evol.func

import com.mayorgraeme.evol.func.EvolveFunc._
import com.mayorgraeme.evol.util.ArrayFunctions._
import com.mayorgraeme.evol._
import com.mayorgraeme.evol.data.java.PlantData
import com.mayorgraeme.evol.data.java.ActorData
import com.mayorgraeme.evol.enums.LocationType._
import java.util.UUID
import scala.collection.immutable.Queue
import com.mayorgraeme.evol.util.GeneticUtil._

case class Seed(species: String, maxAge:Int, sproutTime:Int, size:Int, seedRadius:Int, spermRadius: Int, gender: Char, allowedLocationTypes: Set[LocationType], chanceOfPropogation: Int, chanceOfBreeding: Int, waterNeed:Int, parents: Queue[Plant]) extends Inhabitant {
  var currentAge:Int = 0    
  val uuid = UUID.randomUUID.getMostSignificantBits
    
  override def getActorData(): ActorData = {
    new PlantData(uuid, species, "seed", gender, maxAge, currentAge, sproutTime, size, seedRadius, spermRadius, chanceOfPropogation, chanceOfBreeding, waterNeed)
  }
    
  override def toString = "S"+gender+"-"+uuid
  override def hashCode = uuid.toInt

  
  def similarEnough(seedOne: Seed, seedTwo:Seed): Boolean = {
    geneticSimilarity(seedOne.waterNeed, seedTwo.waterNeed) && 
    geneticSimilarity(seedOne.maxAge, seedTwo.maxAge) && 
    geneticSimilarity(seedOne.chanceOfPropogation, seedTwo.chanceOfPropogation)  && 
    geneticSimilarity(seedOne.chanceOfBreeding, seedTwo.chanceOfBreeding) 
  }
  
  def similarEnough(seed:Seed, plant: Plant): Boolean = {
    geneticSimilarity(plant.waterNeed, seed.waterNeed) && 
    geneticSimilarity(plant.maxAge, seed.maxAge) && 
    geneticSimilarity(plant.chanceOfPropogation, seed.chanceOfPropogation)  && 
    geneticSimilarity(plant.chanceOfBreeding, seed.chanceOfBreeding) 
  }
  
  override def canBreed(other: Inhabitant): Boolean = {
    other match {
      case otherSeed: Seed => this.gender != otherSeed.gender && similarEnough(this, otherSeed) && !this.parents.intersect(otherSeed.parents).isEmpty
      case otherPlant: Plant => this.gender != otherPlant.gender && similarEnough(this, otherPlant) && !this.parents.intersect(otherPlant.parents).isEmpty
      case _ => false  
    }      
  }
    
  override def withUpdatedSpecies(newSpecies: String): Inhabitant = new Seed(newSpecies, maxAge, sproutTime, size, seedRadius, spermRadius, gender, allowedLocationTypes, chanceOfPropogation, chanceOfBreeding, waterNeed, parents)
  
  override def transformWorld(world: World, locationInformation: LocationInformation): World = {
    currentAge = currentAge + 1
    println(currentAge,sproutTime, maxAge)
    if(currentAge>= (sproutTime)){
      replaceInWorld(world, locationInformation.x, locationInformation.y, this, new Plant(species, maxAge, sproutTime, size, seedRadius, spermRadius, gender, allowedLocationTypes, chanceOfPropogation, chanceOfBreeding, waterNeed, parents))
    }else {
      world
    }
  }
}