package com.mayorgraeme.evol

import scala.util.Random
import com.mayorgraeme.evol.util.ArrayFunctions._

class LocationGenerator(x: Int, y:Int) {

  val r = new Random
  
  //Forest Config
  val minNoOfForrests = 20
  val maxNoOfForrests = 25
  val forrestMinRadius = 2
  val forrestMaxRadius = 4
  val percentChanceTree = 100

  //River Config
  val minNoOfRivers = 2
  val maxNoOfRivers = 3
  val percentChanceChangeDirection = 40
  
  val map = Array.fill[Char](x,y)(' ')
  
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
          map(curX)(curY) = '*';
        }
      }
    }
  }
  
  def addARiver = {
    var curX = r.nextInt(x)
    var curY = 0;
    var direction = 0;
    
    while(curY != y){
      map(Math.max(curX-1, 0))(curY) = '~';
      map(curX)(curY) = '~';
      map(Math.min(curX+1, x))(curY) = '~';
    
      if(r.nextInt(100) <= percentChanceChangeDirection){
        direction = r.nextInt(3)
      }
      
      direction match {
        case 0 => curX = Math.max(0, curX-1);
        case 1 => curX = Math.min(curX+1, x);
        case _ => 
      }
      curY = curY+1
    }
  }
}
