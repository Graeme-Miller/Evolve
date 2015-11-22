package com.mayorgraeme.evol.util

import scala.collection.immutable.Queue

object BoundedQueue {
  
  class BoundedQueue[A](queue: Queue[A]) {
  
    def add(a: A, maxSize: Int): Queue[A] = {
      val enqueued = queue.enqueue(a)
      val overSize = enqueued.size - maxSize
      if (overSize > 0) {
        val range: Range = 0 until overSize
        range.foldLeft(enqueued: Queue[A])((a,b) => a.dequeue._2)
      } else enqueued
    }            
  }
  
  implicit def queueToBoundedParentList[A](queue: Queue[A]) = new BoundedQueue[A](queue)
}
