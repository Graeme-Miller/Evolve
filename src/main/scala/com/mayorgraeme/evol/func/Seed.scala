
package com.mayorgraeme.evol.func

import com.mayorgraeme.evol.func.EvolveFunc._
import com.mayorgraeme.evol.util.ArrayFunctions._
import com.mayorgraeme.evol._
import com.mayorgraeme.evol.data.java.PlantData
import com.mayorgraeme.evol.data.java.ActorData
import com.mayorgraeme.evol.enums.LocationType._
import java.util.UUID

case class Seed(maxAge:Int, sproutTime:Int, size:Int, seedRadius:Int, spermRadius: Int, gender: Char, allowedLocationTypes: Set[LocationType], chanceOfPropogation: Int, chanceOfBreeding: Int, waterNeed:Int) extends Inhabitant {
    var currentAge:Int = 0    
    val uuid = UUID.randomUUID.getMostSignificantBits
    
    override def getActorData(): ActorData = {
      new PlantData(uuid, "seed", gender, currentAge, maxAge, sproutTime, size, seedRadius, spermRadius, chanceOfPropogation, chanceOfBreeding, waterNeed)
    }
  
    override def transformWorld(world: World, locationInformation: LocationInformation): World = {
      currentAge = currentAge + 1
      //println(currentAge,sproutTime, maxAge)
      if(currentAge>= (sproutTime)){
        replaceInWorld(world, locationInformation.x, locationInformation.y, this, new Plant(maxAge, sproutTime, size, seedRadius, spermRadius, gender, allowedLocationTypes, chanceOfPropogation, chanceOfBreeding, waterNeed))
      }else {
        world
      }
    }
  }