
package org.mayorgraeme.evolution


import akka.actor.ActorSystem
import akka.actor.Props
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global



object Main {
  val x = 10
  
  def main(args: Array[String]): Unit = {       
    val system = ActorSystem("EvolutionService");
    val locationManagerActor = system.actorOf(Props(new LocationManagerActor(20,50)))       
    val testActors = Array.fill(5)(system.actorOf(Props(new AnimalActor(locationManagerActor))))
    
    system.scheduler.schedule(0 milliseconds,
                              1 seconds)
    {locationManagerActor!Tick; testActors.foreach(_!Tick) }
      
 
  }
}
