package com.mayorgraeme.evol.animal.plant

import com.mayorgraeme.evol.Messages._
import akka.actor.Actor
import akka.actor.ActorRef
import com.mayorgraeme.evol.Messages.GetSouroundingRequest
import com.mayorgraeme.evol.Messages.GetSouroundingResponse
import com.mayorgraeme.evol.Messages.StatusRequest
import com.mayorgraeme.evol.Messages.StatusResponse
import com.mayorgraeme.evol.Messages.Tick
import com.mayorgraeme.evol.data.java.PlantData
import com.mayorgraeme.evol.enums.LocationType._
import java.util.Collections
import java.util.UUID
import scala.util.Random

class PlantImpl(vegativePropagation: Int, 
                plantSustanance: Int,
                maxSize: Int,
                maxAge: Int,
                growthRate: Int,
                acceptableLocationTypes: Set[LocationType]) extends Actor {
  
  var tickSender: Option[ActorRef] = None;
  var numberToGrow = 0;
  var currentAge = 0;
  var currentSize = 0;
  val uuid = UUID.randomUUID.getMostSignificantBits;
  val r = new Random;
    
  def grow = {
    if(currentSize != maxSize){
      currentSize += Math.round(maxSize/growthRate);
    }
    
    currentAge += 1;
    if(currentAge >= maxAge){
      sender!Die       
    }
    
  }
  
  def receive = {
    case Tick => {tickSender=Some(sender); grow; sender!GetSouroundingRequest(1);}
    case GetSouroundingResponse(location: Map[ActorRef, Int], actors: Map[ActorRef, Int]) => {        
        val randInt = r.nextInt(100)+1
        println("randint "+randInt+" vegativePropagation "+vegativePropagation)
        if(randInt <= vegativePropagation){ 
          numberToGrow += 1;
          println("randint in")
          r.shuffle(location.filter(_._2 != 0).keys.toList).foreach(y => {println("randint what are you"); y!WhatAreYou});
        }
      }
    case WhatAreYouResponse(locType: LocationType) => {
        println("GRAEMERESP")
        println("GRAEMERESP "+acceptableLocationTypes + " locType "+locType)
        if(acceptableLocationTypes.contains(locType)){          
          if(numberToGrow > 0){
            tickSender match {
              case Some(x) => {
                  x!TheMiracleOfPlantGrowth(sender)
                  numberToGrow -= 1
                }
            }            
          }
        }
      }
    case StatusRequest => {    
        sender!StatusResponse(new PlantData(uuid, "P1", currentAge, vegativePropagation, plantSustanance, maxSize, growthRate));
      }
    case _ => println("received unknown message")
  }
}


