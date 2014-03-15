
package com.mayorgraeme.evol


import akka.actor.ActorSystem
import akka.actor.Props
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global



object Main {
  val x = 10
  
  def main(args: Array[String]): Unit = {       
    val system = ActorSystem("EvolutionService");
    val locationManagerActor = system.actorOf(Props(new LocationManagerActor(10,25, 2)))       
    
    
    system.scheduler.schedule(0 milliseconds,
                              0.2 seconds)
    {locationManagerActor!Tick}
      
 
  }
}
