package com.mayorgraeme.evol.util

import scala.util.Random
import com.mayorgraeme.evol.enums.LocationType._
import com.mayorgraeme.evol.util.ArrayFunctions._

class LocationGenerator(x: Int, y:Int) {

  val r = new Random
  
  //Forest Config
  val minNoOfForrests = 2
  val maxNoOfForrests = 3
  val forrestMinRadius = 10
  val forrestMaxRadius = 15
  val percentChanceTree = 80

  //River Config
  val minNoOfRivers = 1
  val maxNoOfRivers = 3
  val percentChanceChangeDirection = 10
  val percentChanceChangeSize = 33
  val minRiverSize = 2
  val maxRiverSize = 4
  
  val map: Seq[Seq[LocationType]] = getMap
  
  def getMap: Seq[Seq[LocationType]] = {
    val map = Array.fill[LocationType](x,y)(SAND)
    
    def getInBetween(min: Int, max:Int): Int = {
      val dif = max - min;
      if(dif > 0){
        r.nextInt(max - min) + min - 1 ;
      }else{
        min
      }
    }
  
  
    //functions
    def addAForrest = {
      val centreX = r.nextInt(x);
      val centreY = r.nextInt(y);
      val size = getInBetween(forrestMinRadius, forrestMaxRadius)
      circleMembers(x, y, centreX, centreY, size){ (curX, curY, distance) => {
          if(r.nextInt(100) <= percentChanceTree){
            map(curX)(curY) = SAND;
          }
        }
      }
    }
  
    def addARiver = {
      var curX = r.nextInt(x)
      var curY = 0;
      var direction = 0;
      var size = getInBetween(minRiverSize, maxRiverSize);
    
      while(curY != y){
        
        if(r.nextInt(100) <= percentChanceChangeSize){
          size = getInBetween(minRiverSize, maxRiverSize);
        }
        
        map(curX)(curY) = WATER;
        val amountLeft = size - 1
        var amountUp: Int = Math.floor(amountLeft/2).toInt
        var amountDown: Int = Math.floor(amountLeft/2).toInt
        
        if(amountLeft - amountUp - amountDown > 0){
          if(r.nextInt(2) == 1){
            amountUp = amountUp + 1
          }else {
            amountDown = amountDown + 1            
          }            
        }
        for (amount <- 1 to amountUp) {
          map(Math.max(curX-amount, 0))(curY) = WATER;
        }
        for (amount <- 1 to amountDown) {
          map(Math.min(curX+amount, x-1))(curY) = WATER;
        }
        
        
    
        if(r.nextInt(100) <= percentChanceChangeDirection){
          direction = r.nextInt(3)
        }
      
        direction match {
          case 0 => curX = Math.max(0, curX-1);
          case 1 => curX = Math.min(curX+1, x-1);
          case _ => 
        }
        curY = curY+1
      }
    }
  
    //Algorithm
    val noOfForrests = getInBetween(minNoOfForrests, maxNoOfForrests)
    if(noOfForrests > 0){
      for(x <- 0 to noOfForrests){
        addAForrest
      }
    }
    val noOfRivers = getInBetween(minNoOfRivers, maxNoOfRivers)
    if(noOfRivers > 0){
      for(x <- 0 to noOfRivers){
        addARiver
      }
    }
    
    map.map(_.toSeq).toSeq      
  }
}
