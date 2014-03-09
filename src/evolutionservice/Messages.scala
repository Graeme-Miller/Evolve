
package evolutionservice

import akka.actor.ActorRef

case class RegisterAtActorLocation(location:ActorRef)
case class RegisterAtLocation(x:Int, y:Int)

case class GetSouroundingRequest(radius: Int)
case class GetSouroundingResponse(location: Set[ActorRef], actors: Set[ActorRef])

case class StatusRequest
case class StatusResponse(status: Char)

case class Tick
