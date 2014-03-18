package com.mayorgraeme.evol.web

import javax.ws.rs.Path
import akka.actor.ActorSystem
import akka.actor.Props
import akka.util.Timeout
import com.mayorgraeme.evol.LocationManagerActor
import com.mayorgraeme.evol.Messages.Tick
import com.mayorgraeme.evol.data.SystemInfo
import javax.ws.rs.GET
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import scala.concurrent.duration._
import javax.ws.rs.Produces
import com.mayorgraeme.evol.Messages._
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global

@Path("evolve")
class RestController {
  
  val system = ActorSystem("EvolutionService")
  val locationManagerActor = system.actorOf(Props(new LocationManagerActor(10,25, 4)))           
    
  system.scheduler.schedule(0 milliseconds,
                            0.2 seconds)
  {locationManagerActor!Tick}
  

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def processGet: SystemInfo = {
    println("HELLO WEB WORLD")
    implicit val timeout = Timeout(5 seconds);
    (locationManagerActor?SystemInfoRequest).asInstanceOf[SystemInfo]
  }
  
  
  
}
