package com.mayorgraeme.evol.web

import akka.actor.ActorSystem
import akka.actor.Props
import com.mayorgraeme.evol.LocationManagerActor
import scala.concurrent.duration._

import com.mayorgraeme.evol.Messages.Tick
import scala.concurrent.ExecutionContext.Implicits.global


  
object System {
  val system = ActorSystem("EvolutionService")
  val locationManagerActor = system.actorOf(Props(new LocationManagerActor(40,80, 20)))           
  
  system.scheduler.schedule(0 milliseconds,
                            60 seconds)
  {locationManagerActor!Tick}
}

