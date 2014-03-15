
package com.mayorgraeme.evol

import akka.actor.ActorRef

//registration
case class RegisterAtActorLocation(location:ActorRef)
case class RegisterAtLocation(x:Int, y:Int)
case class RegisterAtRandomLoc
case class Startup

//Movement
case class MoveTowardActor(actor:ActorRef)
case class Die


//Awareness messages
case class GetSouroundingRequest(radius: Int)
case class GetSouroundingResponse(location: Map[ActorRef, Int], actors: Map[ActorRef, Int])

case class StatusRequest
case class StatusResponse(status: Char)


//Animal Location Messages
case class areYouFood
case class yesImFood

case class areYouWater
case class yesImWater

//Animal animal messages
case class wannaFuck(actorType: Char)
case class hellYesIWannaFuck


case class Tick
