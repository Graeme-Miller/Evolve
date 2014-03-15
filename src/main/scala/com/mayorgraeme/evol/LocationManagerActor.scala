
package com.mayorgraeme.evol

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Kill
import akka.actor.Props

import org.jgrapht.graph.SimpleGraph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.Graph
import org.jgrapht.alg.DijkstraShortestPath
import org.jgrapht.alg.DijkstraShortestPath._

import scala.collection.JavaConversions._
import scala.util.Random

class LocationManagerActor(val x:Int, val y:Int, val noActors:Int) extends Actor{    
  val r = new Random()
  
  //Create child actors
  var testActors = collection.mutable.Set.empty[ActorRef]
  (1 to noActors).foreach(x => testActors += (context.actorOf(Props(new BasicAnimalActor))))
  testActors.foreach(_!Startup)
  
  val mdArray = Array.fill(x, y)(context.actorOf(Props[LocationActor]))  
  
  var xyArray = collection.mutable.Map.empty[ActorRef, (Int, Int)]
  var graph = new SimpleGraph[ActorRef, DefaultEdge](classOf[DefaultEdge])
  
  mdArray.view.zipWithIndex.foreach{arrayTuple => {
      arrayTuple._1.view.zipWithIndex.foreach{
        elementTuple => {          
          var x1 = arrayTuple._2
          var y1 = elementTuple._2
          xyArray(elementTuple._1) = (x1,y1) //add to xyArray
        }}}}
  
  
  mdArray.view.zipWithIndex.foreach{arrayTuple => {
      arrayTuple._1.view.zipWithIndex.foreach{
        elementTuple => {          
          var x1 = arrayTuple._2
          var y1 = elementTuple._2
          xyArray(elementTuple._1) = (x1,y1) //add to xyArray
          
          for(x2 <- x1-1 to x1+1){
            for(y2<- (y1-1 to y1+1)){               
              if((x1 != x2 || y1 != y2) //check we are not dealing with the location itself
                 &&  x2 >= 0 && y2 >= 0 && x2 < x && y2 < y){ //cgeck they are in the mdArray range
                val destination = mdArray(x2)(y2)
                
                
                //      println("ReGISTER "+xyArray(elementTuple._1) + " "+xyArray(destination))
                //Add to graph
                graph.addVertex(elementTuple._1)
                graph.addVertex(destination)
                graph.addEdge(elementTuple._1, destination)
              }
            }
          }
        }}}}
  
  val locationStates = mdArray.flatten.map{(_, new LocationState)}.toMap
  var actorsToActors = collection.mutable.Map.empty[ActorRef, ActorRef]
    
  def circleMembers(x1:Int, y1:Int, radius:Int, exclude: ActorRef): (Map[ActorRef, Int], Map[ActorRef, Int]) = {    
    // println(x1+" "+y1+" "+radius)
    var locations = collection.mutable.Map.empty[ActorRef, Int]
    var actors = collection.mutable.Map.empty[ActorRef, Int]
    
    val rSquared = radius*radius
    val minX1 = (x1-radius).max(0)
    val maxX1 = (x1+radius).min(x-1)
    
    val minY1 = (y1-radius).max(0)
    val maxY1 = (y1+radius).min(y-1)    
    
    // println("min max "+ minX1 + " "+maxX1)
    //  println("min max "+ minY1 + " "+maxY1)
    
    for(i <- (minX1 to maxX1)) {
      for(j <- (minY1 to maxY1)) {
        //x and y in range
        val xDist = Math.abs(i-x1)
        val yDist = Math.abs(j-y1)
        val xDistSquared =  xDist * xDist
        val yDistSquared =  yDist * yDist
        val xyDistSquared = xDistSquared + yDistSquared
                
        
        //     print("x1 ("+x1+") y1("+y1+") i("+i+") j("+j+") xDist("+xDist+") yDist("+yDist+") xDistSquared("+xDistSquared+") yDistSquared("+yDistSquared+") xyDistSquared("+xyDistSquared+")  rSquared("+rSquared+")")
        if (xyDistSquared <= rSquared){   //array value in radius          
          val distance = Math.sqrt(xyDistSquared).floor.toInt
          //       println("distance("+distance+")")
          
          locations(mdArray(i)(j)) =  distance            
          actors++= locationStates(mdArray(i)(j)).currentResidents.filter(_ != exclude).map{res => (res,distance)}
        }
        
      }
    }
    return (locations.toMap,actors.toMap)
  }

  def printLine = println(" -"+"-"*y)
  
  def move(location:ActorRef, actor:ActorRef){
    leaveLocation(actor)
    //println("enter "+xyArray(location))
    locationStates(location).currentResidents += actor //add to previous location
    actorsToActors(actor) = location
  }
  
  def leaveLocation(actor:ActorRef){
    var lastLocation = actorsToActors.get(actor)
        
    lastLocation match{
      case Some(i) => {
          //println("leave "+xyArray(i))
          locationStates.get(i) match {
            case Some(j) => {j.currentResidents -= actor}
            case None => {}
          }}
      case None => {}
    }
  }
  
  def receive = {
    case  MoveTowardActor(actor:ActorRef) => {    
        //println("MOVE TOWARD ACTOR "+xyArray(actor))
        val fromlocation = actorsToActors.get(sender)
        val toLocation = Option(actor) //actorsToActors.get(actor)
        fromlocation match {
          case Some(i) => toLocation match {
              case Some(j) => {
//                  println("from loc "+xyArray(i))
//                  println("to loc   "+xyArray(j))
                  val path = DijkstraShortestPath.findPathBetween(graph.asInstanceOf[Graph[Serializable,DefaultEdge]], i, j).filter(x => xyArray(graph.getEdgeTarget(x)) != xyArray(i))                  
                  
//                  path.foreach(i => {print(xyArray(graph.getEdgeTarget(i)))}) 
//                  println
                  
                  if(!path.isEmpty){
                                                         
//                    path.foreach(i => {print(xyArray(graph.getEdgeTarget(i)))}) 
//                    println
                    
                    move(graph.getEdgeTarget(path(0)), sender);

                    //          println("G "+ " to "+xyArray(vertex))
                    
                  }
                }
              case None => Unit
            } case None => Unit
        }        
      }
    case Die => {
        leaveLocation(sender)
        testActors.remove(sender)
        context.stop(sender)
        //sender!Kill
      }
    case RegisterAtRandomLoc => {
        val loc = mdArray(r.nextInt(x))(r.nextInt(y))
        move(loc, sender)
      }
    case RegisterAtActorLocation(location:ActorRef) => {
        move(location, sender)
      }
    case RegisterAtLocation(x:Int, y:Int) => {
        println("register at ("+x +" "+y+")")
        val loc = mdArray(x)(y)
        println("converts to "+xyArray(loc))
        move(mdArray(x)(y), sender)
      }
    case GetSouroundingRequest(radius: Int)  =>  {
        val location = actorsToActors(sender)
        val xyTuple = xyArray(location)
        var x = xyTuple._1
        var y = xyTuple._2
                
        sender!GetSouroundingResponse.tupled(circleMembers(x, y, radius, sender))
        
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
                  print(math.floor((locationStates(actor).currentResidents.size/10)).toInt)
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
        
        testActors.foreach(_!Tick)
      }
    case _ => println("Received unknown message ")
  }  
  
  class LocationState{
    var currentState= '?'
    var currentResidents = collection.mutable.Set.empty[ActorRef]
  }
  
}