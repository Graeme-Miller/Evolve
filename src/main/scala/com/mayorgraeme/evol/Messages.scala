
package com.mayorgraeme.evol

import akka.actor.ActorRef
import com.mayorgraeme.evol.data.java.SystemInfo
import com.mayorgraeme.evol.data.java.ActorData
import com.mayorgraeme.evol.enums.LocationType._;
object Messages {

//registration
  case class RegisterAtActorLocation(location:ActorRef)  
  case class RegisterAtRandomLoc
  
  case class InitLocationType(locType: LocationType)

//Movement
  case class MoveTowardActor(actor:ActorRef)
  case class Die


//Awareness messages
  case class GetSouroundingRequest(radius: Int)
  case class GetSouroundingResponse(location: Map[ActorRef, Int], actors: Map[ActorRef, Int])

  case class StatusRequest
  case class StatusResponse(status: ActorData)


//Animal Location Messages
  case class WhatAreYou
  case class WhatAreYouResponse(locType: LocationType)

//Animal animal messages
  case class WannaFuck(actorType: Char)
  case class HellYesIWannaFuck
  case class Penetrate

  case class TheMiracleOfChildBirth
  case class TheMiracleOfPlantGrowth(location: ActorRef)

  case class Tick
  case class SystemInfoRequest
  case class SystemInfoResponse(info: SystemInfo)
  
  //Plant message
  case class SowPlant(acceptableTypes: Set[LocationType])
}
