
package com.mayorgraeme.evol

import akka.actor.Actor
import com.mayorgraeme.evol.data.java.LocationData
import com.mayorgraeme.evol.Messages._

class LocationActor() extends Actor{
  

  var myLocType = ' ';
  
  def receive = {
    case StatusRequest => sender!StatusResponse(new LocationData(myLocType))
    case InitLocationType(locType) => myLocType = locType
      
    case AreYouFood => {
        if(myLocType == '*'){
          sender!YesImFood
        }
      }
    case AreYouWater =>{
        if(myLocType == '~'){
          sender!YesImWater
        }
      }
    case _ => println("Received unknown message")
  }
  
  override def toString = {
    super.toString + " location"
  }
}
