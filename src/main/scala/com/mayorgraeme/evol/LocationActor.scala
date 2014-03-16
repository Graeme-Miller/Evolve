
package com.mayorgraeme.evol

import akka.actor.Actor
import scala.util.Random
import com.mayorgraeme.evol.Messages._

class LocationActor extends Actor{
  
  val r = new Random()
  val loctype = hashCode.abs%5 match{
    case 0 => '*'
    case 1 => '~'
    case _ => ' '
  }
  
  def receive = {
    case StatusRequest => sender!StatusResponse(loctype)
  
    case AreYouFood => {
        if(loctype == '*'){
          sender!YesImFood
        }
      }
    case AreYouWater =>{
        if(loctype == '~'){
          sender!YesImWater
        }
      }
    case _ => println("Received unknown message")
  }
  
  override def toString = {
    super.toString + " location"
  }
}
