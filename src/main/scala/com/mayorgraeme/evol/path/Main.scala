
package com.mayorgraeme.evol.path

import com.mayorgraeme.evol.func.EvolveFunc
import com.mayorgraeme.evol.func.EvolveFunc._
import com.mayorgraeme.evol.data.java.ActorData
import com.mayorgraeme.evol.enums.LocationType._

object Main {



  def main(args: Array[String]): Unit = {
      
    
    val loc1 = new LocationInformation(WATER, 0, 0, 0, 80, Set(new OneInhab))
    val loc2 = new LocationInformation(WATER, 0, 0, 1, 80, Set(new OneInhab))
    val loc3 = new LocationInformation(WATER, 0, 0, 2, 80, Set(new TwoInhab))
   // val loc4 = new LocationInformation(WATER, 0, 0, 2, 80, Set(new ThreeInhab))
    
    val newWorld= Seq(Seq(loc1, loc2, loc3))
    
    //println(EvolveFunc.getAllInhab((updateSpecies(newWorld)))) //.groupBy[String](_._1.species)
    
   for{locInfo <- updateSpecies(newWorld).flatten; inhab <- locInfo.inhabitants}{println(inhab)}
  }
  
  class OneInhab extends Inhabitant {
    var speciesVar = "g"
    override def getActorData(): ActorData = null        
    override def withUpdatedSpecies(newSpecies: String): Inhabitant = {println("t up"); speciesVar =  newSpecies; this  }
    override def canBreed(other: Inhabitant): Boolean = other match {case trueInhab: OneInhab => true; case _ => false}
    override def species = speciesVar
    override def toString = "OneInhab"
  }
  
   class TwoInhab extends Inhabitant {
     var speciesVar = "g"
    override def getActorData(): ActorData = null        
    override def withUpdatedSpecies(newSpecies: String): Inhabitant = {println("f up"); speciesVar =  newSpecies; this}          
    override def canBreed(other: Inhabitant): Boolean = other match {case trueInhab: TwoInhab => true; case _ => false}
    override def species = speciesVar
    override def toString = "TwoInhab"
  }
  
  class ThreeInhab extends Inhabitant {
    var speciesVar = "g"
    override def getActorData(): ActorData = null        
    override def withUpdatedSpecies(newSpecies: String): Inhabitant = {println("f up"); speciesVar =  newSpecies; this }          
    override def canBreed(other: Inhabitant): Boolean = false
    override def species = speciesVar
    override def toString = "ThreeInhab"
  }

}

