
package com.mayorgraeme.evol

import akka.actor.Actor
import scala.util.Random

class LocationActor extends Actor{
  
  val r = new Random()
  val loctype = hashCode.abs%70 match{
    case 0 => '*'
    case 1 => '~'
    case _ => ' '
  }
  
  def receive = {
    case StatusRequest => sender!StatusResponse(loctype)
  
    case areYouFood => {
        if(loctype == '*'){
          sender!yesImFood
        }
      }
    case areYouWater =>{
        if(loctype == '~'){
          sender!yesImWater
        }
      }
    case _ => println("Received unknown message")
  }
  
  override def toString = {
    super.toString + " location"
  }
}
