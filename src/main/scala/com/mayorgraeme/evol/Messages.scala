
package com.mayorgraeme.evol

import akka.actor.ActorRef

case class RegisterAtActorLocation(location:ActorRef)
case class RegisterAtLocation(x:Int, y:Int)

case class GetSouroundingRequest(radius: Int)
case class GetSouroundingResponse(location: Map[ActorRef, Int], actors: Map[ActorRef, Int])

case class StatusRequest
case class StatusResponse(status: Char)

case class MoveTowardActor(actor:ActorRef)
//case class MoveAwayActor(:ActorRef)

case class Tick
