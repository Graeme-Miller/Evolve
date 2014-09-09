package com.mayorgraeme.evol.func

import com.mayorgraeme.evol.data.java.ActorData
import com.mayorgraeme.evol.data.java.DeathData
import com.mayorgraeme.evol.func.EvolveFunc.Inhabitant
import com.mayorgraeme.evol.func.EvolveFunc.LocationInformation
import com.mayorgraeme.evol.func.EvolveFunc._
import scala.util.Random
import com.mayorgraeme.evol.util.ArrayFunctions._

class SpecterOfDeath(moveX: Int, moveY: Int) extends Inhabitant {
  val r = new Random
  
  val percentChanceOfChangeInDirection = 25 
  
  override def transformWorld(world: World, locationInformation: LocationInformation): World = {
    def getInt = {r.nextInt(3) - 1}        
        
    val (newMoveX, newMoveY) = {
      if(r.nextInt(100) <= percentChanceOfChangeInDirection){        
        (getInt, getInt)
      }else {
        (moveX, moveY)
      }      
    }
    
    val x = locationInformation.x + newMoveX
    val y = locationInformation.y + newMoveY
    
    if(x >= 0 && y >= 0 && world.size > x && world(x).size > y){
      //val removedWorld = world(locationInformation.x)(locationInformation.y).inhabitants.foldLeft(world)((a,b) => subFromWorld(a,locationInformation.x, locationInformation.y, b))     

      val removedWorld = circleMembers[LocationInformation](world, locationInformation.x, locationInformation.y, 7).foldLeft(world)((a,b) => b.inhabitants.foldLeft(a)((c,d) => subFromWorld(c,b.x, b.y, d)))     
      
      moveInWorld(removedWorld, locationInformation.x, locationInformation.y, x, y, this, new SpecterOfDeath(newMoveX,newMoveY)) 
    }else {    
      world
    }
  }
    
  override def getActorData(): ActorData = new DeathData(1l, "death", "deathclazz", 'u')
  override def canBreed(inhabitant: Inhabitant): Boolean = false
  override def species: String = "DEATH"
  override def withUpdatedSpecies(species: String): Inhabitant = this

}

