package com.mayorgraeme.evol.parentage


import com.mayorgraeme.evol.func.EvolveFunc.Inhabitant
import org.junit.runner.RunWith
import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.junit.JUnitRunner

import scala.collection.immutable.Queue

/**
 * Created by graememiller on 22/11/2015.
 */
@RunWith(classOf[JUnitRunner])
class AncestryTest extends FlatSpec with Matchers {


  class TestInhabitant extends Inhabitant {
    override def canBreed(inhabitant: Inhabitant): Boolean = true;

    override def withUpdatedSpecies(species: String): Inhabitant = this;

    override def species: String = "HEYO"
  }

  "An Ancestry" should " work with empty parents " in {
    val emptyQueue =  Queue[Set[Inhabitant]]()
    val ancestryOne = new Ancestry(emptyQueue, 3)
    val ancestryTwo = new Ancestry(emptyQueue, 3)

    val inhabitantOne = new TestInhabitant
    val inhabitantTwo = new TestInhabitant

    ancestryOne.areRelated(ancestryTwo) should be(false)
    val newAncestry = ancestryOne.breed(ancestryTwo, inhabitantOne, inhabitantTwo)

    newAncestry.allAncestors.size should be(2)
    newAncestry.ancestryQueue.size should be(1)
    val newSet = newAncestry.ancestryQueue.apply(0)
    newSet.size should be(2)
    assert(newSet.contains(inhabitantOne))
    assert(newSet.contains(inhabitantTwo))
  }

}
