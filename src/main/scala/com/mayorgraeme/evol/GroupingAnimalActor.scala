//
//package com.mayorgraeme.evol
//
//import akka.actor.Actor
//import akka.actor.ActorRef
//import scala.util.Random
//
//import scala.util.Sorting._
//
//class GroupingAnimalActor(val locationManagerActor: ActorRef) extends Actor {
//  val r = new Random()
//
//  
//  locationManagerActor!RegisterAtLocation(r.nextInt(20), r.nextInt(50))
//  
//  def receive = {
//    case Tick => locationManagerActor!GetSouroundingRequest(500)
//    case GetSouroundingResponse(location: Map[ActorRef, Int], actors: Map[ActorRef, Int]) => {
//        def randLoc = {
//          val filteredLocations = location.filter(_._2 == 1)
//          if(!filteredLocations.isEmpty){
//            val nextInt = r.nextInt(filteredLocations.size)   
//            //        println("r "+nextInt + " "+filteredLocations(filteredLocations.keys.toList(nextInt)))
//            sender!RegisterAtActorLocation(filteredLocations.keys.toList(nextInt))
//          }
//        }
//        
//        
//        //val filterActors=  actors.filter(_._2> 2)
//        
////        if(r.nextInt(25) == 1){
////          randLoc
////        }else
//        
//        if(!actors.isEmpty){
//          var groupedActors  = scala.collection.mutable.Map.empty[Int, Set[ActorRef]] 
//          actors.foreach{x => {
//              if(!groupedActors.contains(x._2)){
//                groupedActors(x._2) = Set.empty[ActorRef]
//              }
//              groupedActors(x._2) += x._1
//            }
//          }
//          
//          val goodlist = groupedActors.toList.sortBy(_._2.size).iterator.next._2.toList;
//            
//          sender!MoveTowardActor(goodlist.toList(r.nextInt(goodlist.size)))  
//        }else{
//          randLoc
//        }
//      }
//    
//    case _ => println("received unknown message")
//  }  
//}