package com.mayorgraeme.evol.parentage

import com.mayorgraeme.evol.func.EvolveFunc.Inhabitant
import scala.collection.immutable.Queue
import com.mayorgraeme.evol.util.BoundedQueue._

/**
 * Created by graememiller on 22/11/2015.
 */
class Ancestry (val ancestryQueue: Queue[Set[Inhabitant]], maxSize: Int) {

  val allAncestors = ancestryQueue.flatten.toSet

  def areRelated(otherAncestry: Ancestry): Boolean = !otherAncestry.allAncestors.intersect(this.allAncestors).isEmpty

  //Used incase the ancestry queue is less than max size
  def setOrEmpty(ancestry: Queue[Set[Inhabitant]], index:Integer): Set[Inhabitant] ={
    if(index < ancestry.size){
      ancestry.apply(index)
    } else {
      Set[Inhabitant]()
    }
  }

  def breed(otherAncestry: Ancestry, parentOne: Inhabitant, parentTwo: Inhabitant): Ancestry = {
    var queueToReturn = Queue[Set[Inhabitant]]()
    for (x <- 1 until maxSize) {
      queueToReturn = queueToReturn.add(setOrEmpty(this.ancestryQueue, x) union setOrEmpty(otherAncestry.ancestryQueue, x), maxSize)
    }
    new Ancestry(queueToReturn.add(Set(parentOne, parentTwo), maxSize), maxSize)
  }

  override def toString: String = ancestryQueue.toString()

}
