
package com.mayorgraeme.evol.web

import com.mayorgraeme.evol.func.EvolveFunc
import com.mayorgraeme.evol.func.EvolveFunc._
import com.mayorgraeme.evol.func.SpecterOfDeath
import com.mayorgraeme.evol.func._
import com.mayorgraeme.evol.func.Seed
import com.mayorgraeme.evol.enums.LocationType._
import scala.collection.immutable.Queue
import scala.util.Random

object RestContext {
  val lock: Object = new Object
  val rand = new Random
    
  var world: World = {
    val dasUberParent1 = Queue[Plant](Plant("1", 10, 3, 1, 2, 4, {if(rand.nextInt(2) == 1) 'M' else 'F' }, Set(SAND), 15, 25, 60, Queue[Plant]()))
    val dasUberParent2 = Queue[Plant](Plant("2", 10, 3, 1, 2, 4, {if(rand.nextInt(2) == 1) 'M' else 'F' }, Set(SAND), 15, 25, 60, Queue[Plant]()))
    val intermittentWorld = fillWithRandom(EvolveFunc.world, Seed("1", 10, 3, 1, 2, 4, {if(rand.nextInt(2) == 1) 'M' else 'F' }, Set(SAND), 15, 25, 60, {if(rand.nextInt(2) == 1) dasUberParent1 else dasUberParent1 }));
    
    fillWithRandom(intermittentWorld, {new SpecterOfDeath(0,0)}, 1)
  }
  
  def updateWorld(newWorld: World) = {
    lock.synchronized {
      world = newWorld
    }
  }
  
  def getWorld: World = {
    lock.synchronized {
      world
    }
  }
  
  class Updater extends Runnable {
    def run(){
      println("start")
      while(true){        
        Thread.sleep(100)
        
        val newWorld = transformWorld(getWorld)
        updateWorld(newWorld)
        
        val inhabitants: Seq[Inhabitant] = {for (locationInformation <- world.flatten) yield (locationInformation.inhabitants)}.flatten
        val plants: Seq[Plant] = inhabitants.collect(_ match {case plant: Plant => plant})
        
        if(plants.size != 0){
          val averageMaxAge = plants.foldLeft(0)((b, a) => {b + a.maxAge})/plants.size
          val averageChanceOfBreeding = plants.foldLeft(0)((b, a) => {b + a.chanceOfBreeding})/plants.size
          val averageChanceOfPropagation = plants.foldLeft(0)((b, a) => {b + a.chanceOfPropogation})/plants.size
          
          val men = plants.filter{_.gender == 'M'}.foldLeft(0)((b, a) => {b + 1})
          val women = plants.filter{_.gender == 'F'}.foldLeft(0)((b, a) => {b + 1})
          
          val menToWomen: Double = {if((men == 0) || (women == 0)) 0 else men/women }
          println("MaxAge", averageMaxAge, "ChanceOfBreeding", averageChanceOfBreeding,  "ChanceOfPropagation", averageChanceOfPropagation, "men", men, "women",women, "menToWomen", menToWomen)
        }
        
        
        
//         inhabitants.filter{x => x match {
//              case _: Plant => true 
//              case _ => false}}
        //printWorld(world)
      }      
    }
  }
  
  
  new Thread(new Updater).start
}
