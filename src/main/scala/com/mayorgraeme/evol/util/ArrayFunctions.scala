package com.mayorgraeme.evol.util

object ArrayFunctions {
  
  def circleMembers[B](data: Seq[Seq[B]], centreX:Int, centreY:Int, radius:Int): Seq[B] = {
    def isInRange(curX:Int, curY:Int): Boolean = {
      val rSquared = radius*radius
      
      val xDist = Math.abs(curX-centreX)
      val yDist = Math.abs(curY-centreY)
      val xDistSquared =  xDist * xDist
      val yDistSquared =  yDist * yDist
      val xyDistSquared = xDistSquared + yDistSquared
                               
      xyDistSquared <= rSquared
    }
    
    val maxX = data.size
    val maxY = data(0).size
    
    
    val minX1 = (centreX-radius).max(0)
    val maxX1 = (centreX+radius).min(maxX-1)
    
    val minY1 = (centreY-radius).max(0)
    val maxY1 = (centreY+radius).min(maxY-1)    
    
    // println("min max "+ minX1 + " "+maxX1)
    //  println("min max "+ minY1 + " "+maxY1)
    
    for{curX <- (minX1 to maxX1)
        curY <- (minY1 to maxY1)
        if isInRange(curX, curY)} 
    yield {data(curX)(curY)}
  }
  
  def circleMembers(maxX:Int, maxY:Int, centreX:Int, centreY:Int, radius:Int)(func:(Int, Int, Int) => Unit) = {    

    
    val rSquared = radius*radius
    val minX1 = (centreX-radius).max(0)
    val maxX1 = (centreX+radius).min(maxX-1)
    
    val minY1 = (centreY-radius).max(0)
    val maxY1 = (centreY+radius).min(maxY-1)    
    
    // println("min max "+ minX1 + " "+maxX1)
    //  println("min max "+ minY1 + " "+maxY1)
    
    for(curX <- (minX1 to maxX1)) {
      for(curY <- (minY1 to maxY1)) {
        //x and y in range
        val xDist = Math.abs(curX-centreX)
        val yDist = Math.abs(curY-centreY)
        val xDistSquared =  xDist * xDist
        val yDistSquared =  yDist * yDist
        val xyDistSquared = xDistSquared + yDistSquared
                
               
        if (xyDistSquared <= rSquared){   //array value in radius          
          val distance = Math.sqrt(xyDistSquared).floor.toInt
          func(curX, curY, distance)
        }        
      }
    }    
  }
}
