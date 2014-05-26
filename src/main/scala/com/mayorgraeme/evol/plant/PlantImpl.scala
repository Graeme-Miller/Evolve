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
import com.mayorgraeme.evol.path.Dijik.Coord
import scala.collection.parallel.mutable

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
        if (currentSize != maxSize) {
            currentSize += Math.round(maxSize / growthRate);
        }

        currentAge += 1;
        if (currentAge >= maxAge) {
            sender ! Die
        }
    }

    var locationsDefined = false;
    var acceptableLocations = collection.mutable.Set[ActorRef]()
    var seenLocations = collection.mutable.Set[ActorRef]()
    var coolDown = 0

    def receive = {
        case Tick => {
            tickSender = Some(sender);
            grow;

            if(!locationsDefined || coolDown == 0){
                sender ! GetSouroundingRequest(1);
            }
        }

        case GetSouroundingResponse(currentLoc: Coord,location: Map[Coord, ActorRef], actors: Map[Coord, Set[ActorRef]]) => {
            if(!locationsDefined){
                location filter(_!=currentLoc) foreach (_._2!WhatAreYou)
            } else {

            }
        }
        case WhatAreYouResponse(locType: LocationType) => {
            seenLocations += sender
            if (acceptableLocationTypes.contains(locType)) {
                acceptableLocations += (sender)
            }
            if(seenLocations.size == 4){
                locationsDefined = true
            }
        }
        case StatusRequest => {
            sender ! StatusResponse(new PlantData(uuid, "P1", currentAge, vegativePropagation, plantSustanance, maxSize, growthRate));
        }
        case _ => println("received unknown message")
    }
}


