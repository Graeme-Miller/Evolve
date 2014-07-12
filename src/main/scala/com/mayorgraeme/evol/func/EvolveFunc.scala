
package com.mayorgraeme.evol.func

import com.mayorgraeme.evol.util.ArrayFunctions._
import com.mayorgraeme.evol.util.LocationGenerator
import com.mayorgraeme.evol._
import com.mayorgraeme.evol.data.java.LocationData
import com.mayorgraeme.evol.data.java.SystemInfo
import com.mayorgraeme.evol.data.java.ActorData
import com.mayorgraeme.evol.enums.LocationType._
import java.util.ArrayList
import java.util.UUID
import scala.util.Random

object EvolveFunc {
  
  val maxX = 40
  val maxY = 60
  val startInhabitants = 30
  
  val MAX_DIST_TO_WATER = 20  
  
  val rand = new Random()
  def percentChance(percent: Int) = rand.nextInt(100) < percent
  
  case class LocationInformation(val locationType: LocationType, uuid:Long, x:Int, y:Int, waterValue:Int, inhabitants: Set[Inhabitant])
  type World = Seq[Seq[LocationInformation]]
  
  val world: World = {
    val arrayWorld = new LocationGenerator(maxX, maxY).map
   
  
    arrayWorld.zipWithIndex.map{ xEntry => 
      val x = xEntry._2
      xEntry._1.zipWithIndex.map { yEntry =>
        val y = yEntry._2
        new LocationInformation(yEntry._1, UUID.randomUUID.getMostSignificantBits,  x, y, getWaterValue(arrayWorld, x, y), Set())
      }.toVector
    }.toVector
  }
  
  //Use this
  def getWaterValue(arrayWorld: Seq[Seq[LocationType]], x:Int, y:Int): Int = {
    val distanceToWater = everExpandingCircleSequence(arrayWorld, x, y).filter(_._1 == WATER)(0)._2
    val constrainedDist = Math.min(distanceToWater, MAX_DIST_TO_WATER)
    val invertedDist: Double = MAX_DIST_TO_WATER - constrainedDist
    
    ((invertedDist/MAX_DIST_TO_WATER) * 100).toInt
  }
    
    
  
  
  def printWorld(world: World) = {
    def getLine: String = " -" + "-" * maxY * 2 + "\n"
    var newString = getLine    
    for(x <- Range(0, maxX)){
      newString = newString + "| "
      for(y <- Range(0, maxY)){
        newString = newString + getLocationInformationChar(world(x)(y))
        newString = newString + " "
      }
      newString = newString + "|\n"
    }    
    newString = newString + getLine
    println(newString)
  }
  
  def getLocationChar(locationInformation: LocationInformation):Char = {
    locationInformation.locationType match {
      case WATER => '~'
      case _ => ' '
    }
  }
  
  def getLocationInformationChar(locationInformation: LocationInformation):Char = {
        
    if(locationInformation.inhabitants.isEmpty){
      getLocationChar(locationInformation)
    } else {            
      locationInformation.inhabitants.toSeq(rand.nextInt(locationInformation.inhabitants.size)) match {
        case Animal() => 'A'
        case _: Seed => '.'
        case _: Plant => '*'
      }
    }
  }
  
  def updateWorld(world: World, x: Int, y: Int, locationInformation: LocationInformation): World = world.updated(x, world(x).updated(y, locationInformation))
    
  def changeWorld(world: World, x: Int, y: Int, setChanger :Set[Inhabitant] => Set[Inhabitant]): World = {
    
    val oldInfo = world(x)(y)
    val newInfo = new LocationInformation(oldInfo.locationType, oldInfo.uuid, oldInfo.x, oldInfo.y, oldInfo.waterValue, setChanger(oldInfo.inhabitants))    
    updateWorld(world, x, y, newInfo)
  }
  
  def addToWorld(world: World, x: Int, y: Int, inhabitant: Inhabitant): World = changeWorld(world, x, y, set => set + inhabitant)
  def subFromWorld(world: World, x: Int, y: Int, inhabitant: Inhabitant): World = changeWorld(world, x, y, set => set - inhabitant)
  def replaceInWorld(world: World, x: Int, y: Int, oldInhabitant: Inhabitant, newInhabitant: Inhabitant): World = changeWorld(world, x, y, set => {set - oldInhabitant + newInhabitant})
  
  
  
  def fillWithRandom(world: World, createFunc: => Inhabitant): World= {
    
    Range(0, startInhabitants).foldLeft(world)((world: World, x:Int) => {
        val randX = rand.nextInt(maxX)
        val randY = rand.nextInt(maxY)
      
        addToWorld(world, randX, randY, createFunc)
      })
  }

  
  abstract class Inhabitant {
    def transformWorld(world: World, locationInformation: LocationInformation): World = world
    def getActorData(): ActorData = null
  }
  case class Animal extends Inhabitant
 
  
  def convertWorldToSystemInfo(world: World): SystemInfo= {
    val array = Array.fill[java.util.List[ActorData]](maxX, maxY)(new ArrayList[ActorData]())
    for (locationInformation <- world.flatten){
      val x = locationInformation.x
      val y = locationInformation.y
      array(x)(y).add(new LocationData(getLocationChar(locationInformation), locationInformation.uuid, locationInformation.waterValue))
        
      for(inhabitant <- locationInformation.inhabitants){
        array(x)(y).add(inhabitant.getActorData)
      }
    }
      
    new SystemInfo(maxX, maxY, array)
  }
  
  
  def transformWorld(worldParameter: World): World = {
    val worldFlat: Seq[LocationInformation] = worldParameter.flatten
    worldFlat.foldLeft(worldParameter){(world: World, locationInformation: LocationInformation) => {   
        //println(locationInformation.inhabitants.size)
        locationInformation.inhabitants.foldLeft(world){(world: World, inhabitant: Inhabitant) =>
          inhabitant.transformWorld(world, locationInformation)
        }
      }
    }
  }

}
