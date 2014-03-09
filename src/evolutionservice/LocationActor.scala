
package evolutionservice

import akka.actor.Actor
import scala.util.Random

class LocationActor extends Actor{
  
  val r = new Random()
  
  def receive = {
    case StatusRequest => sender!StatusResponse{hashCode.abs%2 match{
          case 0 => '-'
          case 1 => '|'}}  
    case _ => println("Received unknown message")
  }
}
