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
    println("processGet in")
    implicit val timeout = Timeout(50 seconds);

    val future = System.locationManagerActor?SystemInfoRequest
    println("processGet OUT")
    Await.result(future, timeout.duration) match {      
      case SystemInfoResponse(x) => {println("processGet RESP"); x}
      case _ => {println("RestController - unknown response"); new SystemInfo(0,0, null)}
    }
  }
  
  
  
}
