
package evolutionservice

import akka.actor.Actor
import akka.actor.ActorRef
import scala.util.Random

class AnimalActor(val locationManagerActor: ActorRef) extends Actor {
  val r = new Random()

  
  locationManagerActor!RegisterAtLocation(r.nextInt(15), r.nextInt(50))
  
  def receive = {
    case Tick => locationManagerActor!GetSouroundingRequest(1)
    case GetSouroundingResponse(location: Map[ActorRef, Int], actors: Map[ActorRef, Int]) => {
        val nextInt = r.nextInt(location.size)
        
        sender!RegisterAtActorLocation(location.toList(nextInt)) //r.shuffle(location).head
      }
    
    case _ => println("received unknown message")
  }  
  
  override def toString = {
    "B"
  }
}