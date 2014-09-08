package com.mayorgraeme.evol.func

import com.mayorgraeme.evol.data.java.ActorData
import com.mayorgraeme.evol.data.java.DeathData
import com.mayorgraeme.evol.func.EvolveFunc.Inhabitant
import com.mayorgraeme.evol.func.EvolveFunc.LocationInformation
import com.mayorgraeme.evol.func.EvolveFunc._
import scala.util.Random

class SpecterOfDeath extends Inhabitant {
  val r = new Random
  
  
  
  override def transformWorld(world: World, locationInformation: LocationInformation): World = {
    def getInt = {r.nextInt(3) - 1}        
    
    val x = locationInformation.x + getInt
    val y = locationInformation.y + getInt        
    
    if(x >= 0 && y >= 0 && world.size > x && world(x).size > y){      
      val removedWorld = world(x)(y).inhabitants.foldLeft(world)((a,b) => subFromWorld(a, x, y, b))     
      
      val newWorld = {
        if(r.nextInt(40) == 1){
          addToWorld(removedWorld, locationInformation.x, locationInformation.y, new SpecterOfDeath())
        }else {
          removedWorld
        }
      }
      
      moveInWorld(newWorld, locationInformation.x, locationInformation.y, x, y, this, this) 
    }else {    
      world
    }
  }
    
  override def getActorData(): ActorData = new DeathData(1l, "death", "deathclazz", 'u')
  override def canBreed(inhabitant: Inhabitant): Boolean = false
  override def species: String = "DEATH"
  override def withUpdatedSpecies(species: String): Inhabitant = this

}
