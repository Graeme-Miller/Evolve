package com.mayorgraeme.evol.util

import scala.collection.immutable.Queue

object BoundedParentQueue {
  
  class BoundedParentQueue[A](queue: Queue[A]) { 
  
    def add(a: A, maxSize: Int): Queue[A] = {
      var enqueued = queue.enqueue(a)
      val overSize = enqueued.size - maxSize
      if (overSize > 0) {
        val range: Range = 0 until overSize
        range.foldLeft(enqueued: Queue[A])((a,b) => a.dequeue._2)
      } else enqueued
    }            
  }
  
  implicit def queueToBoundedParentList[A](queue: Queue[A]) = new BoundedParentQueue[A](queue)
}
