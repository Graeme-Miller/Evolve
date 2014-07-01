package com.mayorgraeme.evol.web

import javax.ws.rs.Path
import com.mayorgraeme.evol.data.java.SystemInfo
import com.mayorgraeme.evol.func.EvolveFunc
import com.mayorgraeme.evol.func.EvolveFunc._
import javax.ws.rs.GET
import javax.ws.rs.core.MediaType
import scala.concurrent.duration._
import javax.ws.rs.Produces
import com.mayorgraeme.evol.Messages._



@Path("evolve")
class RestController {

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def processGet: SystemInfo = {    
    convertWorldToSystemInfo(RestContext.getWorld)
  }
}
