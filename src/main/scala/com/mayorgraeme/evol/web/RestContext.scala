
package com.mayorgraeme.evol.web

import com.mayorgraeme.evol.func.EvolveFunc
import com.mayorgraeme.evol.func.EvolveFunc._
import com.mayorgraeme.evol.func.Seed

object RestContext {
  val lock: Object = new Object
  
  var world: World = fillWithRandom(EvolveFunc.world, Seed(SEED_SPROUT_TIME, MAX_AGE));
  
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
        Thread.sleep(1000)
        
        val newWorld = transformWorld(getWorld)
        updateWorld(newWorld)
        printWorld(world)
      }      
    }
  }
  
  
  new Thread(new Updater).start
}
