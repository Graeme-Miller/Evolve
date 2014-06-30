
package com.mayorgraeme.evol.func

import com.mayorgraeme.evol.util.ArrayFunctions._
import com.mayorgraeme.evol.LocationGenerator
import com.mayorgraeme.evol._
import com.mayorgraeme.evol.enums.LocationType._
import scala.util.Random

object EvolveFunc {
  
  val maxX = 25
  val maxY = 25
  val startInhabitants = 4
  val SEED_SPROUT_TIME = 2
  val MAX_AGE = 6
  
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
    def getLine: String = " -" + "-" * maxY * 2 + "\n"
    var newString = getLine    
    for(x <- Range(0, maxX)){
      newString = newString + "| "
      for(y <- Range(0, maxY)){
        newString = newString + getLocationString(world(x)(y))
        newString = newString + " "
      }
      newString = newString + "|\n"
    }    
    newString = newString + getLine
    println(newString)
  }
  
  def getLocationString(locationInformation: LocationInformation):String = {
        
    if(locationInformation.inhabitants.isEmpty){
      locationInformation.locationType match {
        case WATER => "~"
        case _ => " "
      }
    } else {            
      locationInformation.inhabitants.toSeq(rand.nextInt(locationInformation.inhabitants.size)) match {
        case Animal() => "A"
        case Seed(_, _) => "."
        case Plant(_) => "*"
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
    
    Range(0, startInhabitants).foldLeft(world)((world: World, x:Int) => {
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
      //println(currentAge,sproutTime, maxAge)
      if(currentAge>= (sproutTime)){
        replaceInWorld(world, locationInformation.x, locationInformation.y, this, new Plant(maxAge - sproutTime))
      }else {
        world
      }
    }
    
  }
  case class Plant(maxAge:Int) extends Inhabitant {
    val chanceOfPropogation = 50
    var currentAge = 0
    val allowedLocationTypes = Set(SAND)
    
    override def transformWorld(world: World, locationInformation: LocationInformation): World = {
      currentAge = currentAge + 1
      
      //println(currentAge, maxAge)
      if(currentAge >= maxAge) {
        subFromWorld(world, locationInformation.x, locationInformation.y, this)
      } else if(percentChance(chanceOfPropogation)){
        //println("TREE TIME "+circleMembers[LocationInformation](world, locationInformation.x, locationInformation.y, 1).size)
        val spacesWithoutPlants: Seq[LocationInformation] = circleMembers[LocationInformation](world, locationInformation.x, locationInformation.y, 1).filter{g => 
          allowedLocationTypes.contains(g.locationType) && !g.equals(locationInformation) && g.inhabitants.forall{ g => 
            g match {
              case Plant(_) => false
              case Seed(_, _) => false
              case _ => true
            }
          }}
        
        if(!spacesWithoutPlants.isEmpty){
          val space = spacesWithoutPlants(rand.nextInt(spacesWithoutPlants.size))
          addToWorld(world, space.x, space.y, new Seed(SEED_SPROUT_TIME, MAX_AGE))
        } else {
          world
        }
      } else {
        world
      }
    }
  }
  
  
  
  def main(args: Array[String]): Unit = {
        
    var worldVar = fillWithRandom(world, Seed(SEED_SPROUT_TIME, MAX_AGE))
    while(true){
      Thread.sleep(1000)
      val worldFlat: Seq[LocationInformation] = worldVar.flatten
      worldVar = worldFlat.foldLeft(worldVar){(world: World, locationInformation: LocationInformation) => {   
          //println(locationInformation.inhabitants.size)
          locationInformation.inhabitants.foldLeft(world){(world: World, inhabitant: Inhabitant) =>
            inhabitant.transformWorld(world, locationInformation)
          }
        }
      }
      
      printWorld(worldVar)
    }    
  }
}
