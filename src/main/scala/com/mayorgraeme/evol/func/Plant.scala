
package com.mayorgraeme.evol.func

import com.mayorgraeme.evol.func.EvolveFunc._
import com.mayorgraeme.evol.util.ArrayFunctions._
import com.mayorgraeme.evol._
import com.mayorgraeme.evol.data.java.PlantData
import com.mayorgraeme.evol.data.java.ActorData
import com.mayorgraeme.evol.enums.LocationType._
import java.util.UUID


case class Plant(maxAge:Int) extends Inhabitant {
  val uuid = UUID.randomUUID.getMostSignificantBits
  val chanceOfPropogation = 50
  var currentAge = 0
  val allowedLocationTypes = Set(WATER)
    
  override def getActorData(): ActorData = {
    new PlantData(uuid, "plant", currentAge, chanceOfPropogation, 0, 0, 0)
  }
    
  override def transformWorld(world: World, locationInformation: LocationInformation): World = {
    currentAge = currentAge + 1
      
    //println(currentAge, maxAge)
    if(currentAge >= maxAge) {
      subFromWorld(world, locationInformation.x, locationInformation.y, this)
    } else if(percentChance(chanceOfPropogation)){
      //println("TREE TIME "+circleMembers[LocationInformation](world, locationInformation.x, locationInformation.y, 1).size)
      val spacesWithoutPlants: Seq[LocationInformation] = circleMembers[LocationInformation](world, locationInformation.x, locationInformation.y, 1).filter{g => 
        allowedLocationTypes.contains(g.locationType) && !g.equals(locationInformation) && g.inhabitants.forall{ g => 
          g match {
            case Plant(_) => false
            case Seed(_, _) => false
            case _ => true
          }
        }}
        
      if(!spacesWithoutPlants.isEmpty){
        val space = spacesWithoutPlants(rand.nextInt(spacesWithoutPlants.size))
        addToWorld(world, space.x, space.y, new Seed(SEED_SPROUT_TIME, MAX_AGE))
      } else {
        world
      }
    } else {
      world
    }
  }
}
