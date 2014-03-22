package com.mayorgraeme.evol.web

import javax.ws.rs.Path
import akka.util.Timeout
import com.mayorgraeme.evol.data.java.SystemInfo
import javax.ws.rs.GET
import javax.ws.rs.core.MediaType
import scala.concurrent.duration._
import javax.ws.rs.Produces
import com.mayorgraeme.evol.Messages._
import akka.pattern.ask
import scala.concurrent.Await



@Path("evolve")
class RestController {

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def processGet: SystemInfo = {    

    // return new SystemInfo(13, 12, null)
    implicit val timeout = Timeout(5 seconds);

    val future = System.locationManagerActor?SystemInfoRequest
    Await.result(future, timeout.duration) match {
      
      case SystemInfoResponse(x) => {x}
      case _ => {println("RestController - unknown response"); new SystemInfo(0,0, null)}
    }
  }
  
  
  
}
