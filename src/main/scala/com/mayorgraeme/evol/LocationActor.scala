
package com.mayorgraeme.evol

import akka.actor.Actor
import com.mayorgraeme.evol.data.java.LocationData
import com.mayorgraeme.evol.Messages._
import com.mayorgraeme.evol.enums.LocationType._;
import java.util.UUID

class LocationActor() extends Actor{
  

  var myLocType = UNKNOWN;
  val uuid = UUID.randomUUID.getMostSignificantBits;
  
  def receive = {
    case StatusRequest => {
        var charToSend = ' ';
        if(myLocType == WATER){
          charToSend = '~'
        }else if (myLocType == SAND){
          charToSend = ' '
        }
        sender!StatusResponse(new LocationData(charToSend, uuid))
      }
    case InitLocationType(locType) => myLocType = locType
      
//    case AreYouFood => {
//        if(myLocType == '*'){
//          sender!YesImFood
//        }
//      }
//    case AreYouWater =>{
//        if(myLocType == WATER){
//          sender!YesImWater
//        }
//      }
    case WhatAreYou =>{        
        sender!WhatAreYouResponse(myLocType)        
      }
    case _ => println("Received unknown message")
  }
  
  override def toString = {
    super.toString + " location"
  }
}
