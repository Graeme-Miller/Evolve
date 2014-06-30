
package com.mayorgraeme.evol.func

import com.mayorgraeme.evol.util.ArrayFunctions._
import com.mayorgraeme.evol.LocationGenerator
import com.mayorgraeme.evol._
import com.mayorgraeme.evol.enums.LocationType._
import scala.util.Random

object EvolveFunc {
  
  val maxX = 1
  val maxY = 1
  val startInhabitants = 1
  val seedSproutTime = 10
  val maxAge = 50
  
  val rand = new Random()
  def percentChance(percent: Int) = rand.nextInt(100) < percent
  
  case class LocationInformation(val locationType: LocationType, x:Int, y:Int, inhabitants: Set[Inhabitant])
  type World = Seq[Seq[LocationInformation]]
  
  val world: World = {
    val arrayWorld = new LocationGenerator(maxX, maxY).map.clone
   
  
    arrayWorld.zipWithIndex.map{ xEntry => 
      val x = xEntry._2
      xEntry._1.zipWithIndex.map { yEntry =>
        val y = yEntry._2
        new LocationInformation(yEntry._1, x, y, Set())
      }.toVector
    }.toVector
  }
  
  def printWorld(world: World) = {
    def printLine = println(" -" + "-" * maxY * 2)
    printLine    
    for(x <- Range(0, maxX)){
      print("| ")
      for(y <- Range(0, maxY)){
        printLocation(world(x)(y))
        print(" ")
      }
      println("|")
    }    
    printLine
  }
  
  def printLocation(locationInformation: LocationInformation) = {
        
    if(locationInformation.inhabitants.isEmpty){
      locationInformation.locationType match {
        case WATER => print("=")
        case _ => print(" ")
      }
    } else {            
      locationInformation.inhabitants.toSeq(rand.nextInt(locationInformation.inhabitants.size)) match {
        case Animal() => {print("A")}
        case Seed(_, _) => {print("S")}
        case Plant(_) => {print("P")}
      }
    }
  }
  
  def updateWorld(world: World, x: Int, y: Int, locationInformation: LocationInformation): World = world.updated(x, world(x).updated(y, locationInformation))
    
  def changeWorld(world: World, x: Int, y: Int, setChanger :Set[Inhabitant] => Set[Inhabitant]): World = {
    
    val oldInfo = world(x)(y)
    val newInfo = new LocationInformation(oldInfo.locationType, oldInfo.x, oldInfo.y, setChanger(oldInfo.inhabitants))    
    updateWorld(world, x, y, newInfo)
  }
  
  def addToWorld(world: World, x: Int, y: Int, inhabitant: Inhabitant): World = changeWorld(world, x, y, set => set + inhabitant)
  def subFromWorld(world: World, x: Int, y: Int, inhabitant: Inhabitant): World = changeWorld(world, x, y, set => set - inhabitant)
  def replaceInWorld(world: World, x: Int, y: Int, oldInhabitant: Inhabitant, newInhabitant: Inhabitant): World = changeWorld(world, x, y, set => {set - oldInhabitant + newInhabitant})
  
  
  
  def fillWithRandom(world: World, createFunc: => Inhabitant): World= {
    
    Range(0, startInhabitants+1).foldLeft(world)((world: World, x:Int) => {
        val randX = rand.nextInt(maxX)
        val randY = rand.nextInt(maxY)
      
        addToWorld(world, randX, randY, createFunc)
      })
  }

  
  abstract class Inhabitant {
    def transformWorld(world: World, locationInformation: LocationInformation): World = world
  }
  case class Animal extends Inhabitant
  
  case class Seed(sproutTime:Int, maxAge: Int) extends Inhabitant {
    var currentAge:Int = 0
  
    override def transformWorld(world: World, locationInformation: LocationInformation): World = {
      currentAge = currentAge + 1
      println(currentAge + " " +  sproutTime + " " + maxAge)
      if(currentAge>= (sproutTime)){
        replaceInWorld(world, locationInformation.x, locationInformation.y, this, new Plant(maxAge - sproutTime))
      }else {
        world
      }
    }
    
  }
  case class Plant(maxAge:Int) extends Inhabitant {
    val chanceOfPropogation = 10
    var currentAge = 0
    
    override def transformWorld(world: World, locationInformation: LocationInformation): World = {
      currentAge = currentAge + 1
      if(currentAge equals maxAge) {
        subFromWorld(world, locationInformation.x, locationInformation.y, this)
      } else if(percentChance(chanceOfPropogation)){
        
        val spacesWithoutPlants: Seq[LocationInformation] = circleMembers[LocationInformation](world, locationInformation.x, locationInformation.y, 1).filter{g => 
          g.equals(locationInformation) || g.inhabitants.forall{ g => 
            g match {
              case Plant(_) => false
              case _ => true
            }
          }}
        
        if(!spacesWithoutPlants.isEmpty){
          val space = spacesWithoutPlants(rand.nextInt(spacesWithoutPlants.size))
          addToWorld(world, locationInformation.x, locationInformation.y, new Seed(seedSproutTime, maxAge))
        } else {
          world
        }
      } else {
        world
      }
    }
  }
  
  
  
  def main(args: Array[String]): Unit = {
        
    var worldVar = fillWithRandom(world, Seed(seedSproutTime, maxAge))
    while(true){
      Thread.sleep(1000)
      val worldFlat: Seq[LocationInformation] = worldVar.flatten
      worldVar = worldFlat.foldLeft(world){(world: World, locationInformation: LocationInformation) => {
          locationInformation.inhabitants.foldLeft(world){(world: World, inhabitant: Inhabitant) =>
            inhabitant.transformWorld(world, locationInformation)
          }
        }
      }
      
      printWorld(worldVar)
    }    
  }
}
