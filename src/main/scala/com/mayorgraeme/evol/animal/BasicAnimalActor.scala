
package com.mayorgraeme.evol.animal

import akka.actor.Actor
import akka.actor.ActorRef
import scala.util.Random
import com.mayorgraeme.evol.Messages._

import scala.util.Sorting._

class BasicAnimalActor extends Actor {
  val r = new Random()
  
  var hunger = 40//80 + r.nextInt(20)
  var thirst = 80 + r.nextInt(20)
  var sex = r.nextInt(100)

  var food = Map.empty[ActorRef, Int]
  var water = Map.empty[ActorRef, Int]
  var fuckBuddies = Map.empty[ActorRef, Int]
  
  val maxAge = 150 + r.nextInt(50)
  var currentAge = 0
    
  val gender = r.nextInt(2) match {
    case 0 => 'M'
    case _ => 'F'
  }
  
  var pregnant = false
  var pregnancyCountdown = 0
  val gestation = 20

  def degradeCollection(col: Map[ActorRef, Int]): Map[ActorRef, Int] = {
    col.view.map(x=>(x._1, x._2 -1)).filter(_._2 != 0).toMap
  }
  
  def stateUpdate(sender: ActorRef) = {
    hunger = Math.max(hunger - r.nextInt(5), 0)
    thirst = Math.max(thirst - r.nextInt(5), 0)
    if(!pregnant){
      sex = Math.min(sex + r.nextInt(5), 100)
    }
    
    println("gender ("+gender+") pregnant("+pregnant+") pregnancyCountdown("+pregnancyCountdown+") hunger(" +hunger+") thirst(" +thirst+") sex(" +sex+") food size("+food.size+") water size("+water.size+") fuckBuddies size("+fuckBuddies.size+") age("+currentAge+"/"+maxAge+")")
    
    if(hunger == 0 || thirst  == 0){
      println("DIE DIE DIE")
      sender!Die
    }
    
    food = degradeCollection(food)
    water= degradeCollection(water)
    fuckBuddies = degradeCollection(fuckBuddies)
    
  }
  
  def searchFood(location: Map[ActorRef, Int]){
    location.foreach(_._1!AreYouFood)
    
  }
  def searchWater(location: Map[ActorRef, Int]){
    location.foreach(_._1!AreYouWater)
    
  }
  def serachForFuckBuddies(actor: Map[ActorRef, Int]){
    actor.foreach(_._1!WannaFuck(gender))
  }
  def randLoc(location: Map[ActorRef, Int]) = {
    val filteredLocations = location.filter(_._2 == 1)
    if(!filteredLocations.isEmpty){
      val nextInt = r.nextInt(filteredLocations.size)   
      
      sender!RegisterAtActorLocation(filteredLocations.keys.toList(nextInt))
    }
  }
  
  def getClosestActor(actors: Map[ActorRef, Int], memory: Map[ActorRef, Int]): Option[(ActorRef,Int)] = {
    val intersection = actors.keySet.intersect(memory.keySet)    
    
    val list = actors.view.filter(x => intersection.contains(x._1)).toList.sortBy(_._2)
    if(list.isEmpty){
      None
    }else{
      Some(list.head)
    }
  }
  def checkActorMoveOrDo(option: Option[(ActorRef,Int)], actor:ActorRef, function:() => Unit, elseFunction:() => Unit) = {
    option match{
      case Some(x) => {
          if(x._2 > 1){
            actor!MoveTowardActor(x._1)
          }else{
            function()
          }
        }
      case None => {elseFunction()}
    }
  }
  
  def receive = {
    case Startup => sender!RegisterAtRandomLoc
    case Tick => {stateUpdate(sender); sender!GetSouroundingRequest(10);}
    case GetSouroundingResponse(location: Map[ActorRef, Int], actors: Map[ActorRef, Int]) => {
        if(pregnant){
          pregnancyCountdown = pregnancyCountdown - 1
        }
        
        searchFood(location)
        searchWater(location)
        serachForFuckBuddies(actors)
        
        currentAge = currentAge + 1
        
        if (currentAge >= maxAge) {
          sender ! Die
        }else if(pregnant && pregnancyCountdown <= 0){
          pregnant = false
          sender!TheMiracleOfChildBirth
        }else if(hunger < 50 || thirst < 50){
          if(hunger < thirst){            
            val actor = getClosestActor(location, food)            
            checkActorMoveOrDo(actor, sender, () => {hunger += r.nextInt(50)}, () => randLoc(location))
          }else{           
            val actor = getClosestActor(location, water)            
            checkActorMoveOrDo(actor, sender, () => {thirst += r.nextInt(50)}, () => randLoc(location))
          }
        }else if (sex > 80){          
          val actor = getClosestActor(actors, fuckBuddies)
          checkActorMoveOrDo(actor, sender, () => {
              if(gender == 'M'){
                actor.get._1!Penetrate
                sex = 0
              }
            }, () => randLoc(location))
        }else{
          randLoc(location)
        }
      }
    case YesImFood => {
        food += ((sender, 5))
      }
    case YesImWater => {
        water += ((sender, 5))
      }
    case HellYesIWannaFuck => {
        fuckBuddies += ((sender, 5))
      }
    case WannaFuck(x) =>{
        if (x != gender  && !pregnant && sex > 80){
          sender!HellYesIWannaFuck
        }
      }
    
    case Penetrate => {
        if(gender == 'F'){
          pregnant = true
          pregnancyCountdown = gestation
          sex = 0        
        }
      }
    case _ => println("received unknown message")
  }  
}
