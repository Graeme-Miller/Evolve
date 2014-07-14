
package com.mayorgraeme.evol.path

import com.mayorgraeme.evol.func.EvolveFunc._
import com.mayorgraeme.evol.data.java.ActorData
import com.mayorgraeme.evol.enums.LocationType._

object Main {



  def main(args: Array[String]): Unit = {
      
    
    val loc1 = new LocationInformation(WATER, 0, 0, 2, 80, Set(new OneInhab))
    val loc2 = new LocationInformation(WATER, 0, 0, 2, 80, Set(new OneInhab))
    val loc3 = new LocationInformation(WATER, 0, 0, 2, 80, Set(new TwoInhab))
    
    val newWorld= Seq(Seq(loc1, loc2, loc3))
    
    updateSpecies(newWorld)
    
   
  }
  
  class OneInhab extends Inhabitant {
    override def getActorData(): ActorData = null        
    override def withUpdatedSpecies(newSpecies: String): Inhabitant = {println("t up"); this  }
    override def canBreed(other: Inhabitant): Boolean = other match {case trueInhab: OneInhab => false; case _ => true}
    override def species = "g"
    override def toString = "t"
  }
  
   class TwoInhab extends Inhabitant {
    override def getActorData(): ActorData = null        
    override def withUpdatedSpecies(newSpecies: String): Inhabitant = {println("f up"); this  }          
    override def canBreed(other: Inhabitant): Boolean = other match {case trueInhab: TwoInhab => false; case _ => true}
    override def species = "g"
    override def toString = "f"
  }

}

