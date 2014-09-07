package com.mayorgraeme.evol.util

import scala.util.Random

object GeneticUtil {

  val rand = new Random()
  
  val similarityIndex = 5;
  
  def geneticTransformation(first: Int, second: Int): Int = {
    val possibleMin: Int = Math.min(first, second)
    val possibleMax: Int = Math.max(first, second)
    
    val minDelta = possibleMin * 0.001
    val maxDelta = possibleMax * 0.001
    
    val min: Int = Math.max(1, Math.floor(possibleMin - minDelta).toInt)
    val max: Int = Math.min(100, Math.ceil(possibleMax + maxDelta).toInt)
    
    rand.nextInt(max-min+1) + min
  }
  
    
  def geneticSimilarity(first: Int, second: Int): Boolean = {
    val delta = Math.abs(second - first)    
    
    delta < similarityIndex      
  }
  
  
}
