
package com.mayorgraeme.evol.func

import com.mayorgraeme.evol.func.EvolveFunc._
import com.mayorgraeme.evol.util.ArrayFunctions._
import com.mayorgraeme.evol._
import com.mayorgraeme.evol.data.java.PlantData
import com.mayorgraeme.evol.data.java.ActorData
import com.mayorgraeme.evol.enums.LocationType._
import java.util.UUID

case class Seed(sproutTime:Int, maxAge: Int) extends Inhabitant {
    var currentAge:Int = 0    
    val uuid = UUID.randomUUID.getMostSignificantBits
    
    override def getActorData(): ActorData = {
      new PlantData(uuid, "seed", currentAge, 0, 0, 0, 0)
    }
  
    override def transformWorld(world: World, locationInformation: LocationInformation): World = {
      currentAge = currentAge + 1
      //println(currentAge,sproutTime, maxAge)
      if(currentAge>= (sproutTime)){
        replaceInWorld(world, locationInformation.x, locationInformation.y, this, new Plant(maxAge - sproutTime))
      }else {
        world
      }
    }
    
  }