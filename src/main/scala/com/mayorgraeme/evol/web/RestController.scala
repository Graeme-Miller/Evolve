package com.mayorgraeme.evol.web

import javax.ws.rs.Path
import akka.util.Timeout
import com.mayorgraeme.evol.data.java.SystemInfo
import com.mayorgraeme.evol.func.EvolveFunc
import com.mayorgraeme.evol.func.EvolveFunc._
import javax.ws.rs.GET
import javax.ws.rs.core.MediaType
import scala.concurrent.duration._
import javax.ws.rs.Produces
import com.mayorgraeme.evol.Messages._
import akka.pattern.ask
import scala.concurrent.Await



@Path("evolve")
class RestController {
  
  val lock: Object = new Object
  
  var world: World = EvolveFunc.world;
  
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
      while(true){
        Thread.sleep(1000)
        val newWorld = transformWorld(getWorld)
        updateWorld(newWorld)
        printWorld(newWorld)
      }      
    }
  }
  
  
  new Thread(new Updater).start

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def processGet: SystemInfo = {    
    convertWorldToSystemInfo(getWorld)
  }
}
