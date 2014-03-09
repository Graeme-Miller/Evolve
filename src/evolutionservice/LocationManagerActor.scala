
package evolutionservice

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props

class LocationManagerActor(val x:Int, val y:Int) extends Actor{    
  
  val mdArray = Array.fill(x, y)(context.actorOf(Props[LocationActor]))  
  
  var xyArray = collection.mutable.Map.empty[ActorRef, (Int, Int)]
  
  mdArray.view.zipWithIndex.foreach{arrayTuple => {
      arrayTuple._1.view.zipWithIndex.foreach{
        elementTuple => {           
          xyArray(elementTuple._1) = (arrayTuple._2, elementTuple._2)
        }}}}
  
  val locationStates = mdArray.flatten.map{(_, new LocationState)}.toMap
  var actorsToActors = collection.mutable.Map.empty[ActorRef, ActorRef]
    
  def circleMembers(x1:Int, y1:Int, radius:Int): (Map[ActorRef, Int], Map[ActorRef, Int]) = {    
    var locations = collection.mutable.Map.empty[ActorRef, Int]
    var actors = collection.mutable.Map.empty[ActorRef, Int]
    
    val rSquared = radius.^(2)
    
    
    for(i <- (x1-radius to x1+radius)) {
      for(j <- (y1-radius to y1+radius)) {
        if((i-radius >= 0 && i+radius <= x)  && (j-radius >= 0 && j+radius <= y )){ //x and y in range
          val xDist = i-x
          val yDist = j-y
          val xDistSquared =  xDist.^(2)
          val yDistSquared =  yDist.^(2)
          val xyDistSquared = xDistSquared + yDistSquared
          if (xyDistSquared <= rSquared){   //array value in radius
            val distance = Math.sqrt(xyDistSquared).floor.toInt
            locations(mdArray(i)(j)) =  distance            
            actors++= locationStates(mdArray(i)(j)).currentResidents.map{res => (res,distance)}
          }
        }
      }
    }
    return (locations.toMap,actors.toMap)
  }

  def printLine = println(" -"+"-"*y)
  
  def move(location:ActorRef, actor:ActorRef){
    var lastLocation = actorsToActors.get(actor)
    
    lastLocation match{
      case Some(i) => locationStates.get(i) match {
          case Some(j) => j.currentResidents -= actor
          case None => {}
        }
      case None => {}
    }
    locationStates(location).currentResidents += actor //add to previous location
    actorsToActors(actor) = location
  }
  
  def receive = {
    case RegisterAtActorLocation(location:ActorRef) => {
        move(location, sender)
      }
    case RegisterAtLocation(x:Int, y:Int) => {
        move(mdArray(x)(y), sender)
      }
    case GetSouroundingRequest(radius: Int)  =>  {
        val location = actorsToActors(sender)
        val xyTuple = xyArray(location)
        var x = xyTuple._1
        var y = xyTuple._2
                
        sender!GetSouroundingResponse.tupled(circleMembers(x, y, radius))
        
      }
    case StatusResponse(status: Char) => locationStates(sender).currentState = status
    case Tick => {
        mdArray.flatten.foreach(_!StatusRequest)
        printLine
        mdArray.foreach {array => {
            print("|")
            array.foreach{actor => {
                if(!locationStates(actor).currentResidents.isEmpty){
                  //print(locationStates(actor).currentResidents.head)
                  print("O")
                }else {
                  locationStates.get(actor) match {
                    case Some(i) => print(i.currentState)
                    case None => print(" ")
                  }  
                }
              }}
            println(" |" )
          }
        }
        printLine
      }
    case _ => println("Received unknown message ")
  }  
  
  class LocationState{
    var currentState= '?'
    var currentResidents = collection.mutable.Set.empty[ActorRef]
  }
  
}