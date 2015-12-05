package com.mayorgraeme.evol.parentage


import com.mayorgraeme.evol.func.EvolveFunc.Inhabitant
import org.junit.runner.RunWith
import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.junit.JUnitRunner

import scala.collection.immutable
import scala.collection.immutable.{IndexedSeq, Queue}

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
    val emptyQueue = Queue[Set[Inhabitant]]()
    val ancestryOne = new Ancestry(emptyQueue, 3)
    val ancestryTwo = new Ancestry(emptyQueue, 3)

    val inhabitantOne = new TestInhabitant
    val inhabitantTwo = new TestInhabitant

    ancestryOne.areRelated(ancestryTwo) should be(false)
    val newAncestry = ancestryOne.breed(ancestryTwo, inhabitantOne, inhabitantTwo)

    newAncestry.allAncestors.size should be(2)
    newAncestry.ancestryQueue.size should be(3)
    val newSet = newAncestry.ancestryQueue.last
    print(newAncestry)
    newSet.size should be(2)
    assert(newSet.contains(inhabitantOne))
    assert(newSet.contains(inhabitantTwo))
  }

  "An Ancestry" should " work only one parent " in {
    val parent = new TestInhabitant

    val emptyQueue = Queue[Set[Inhabitant]]()
    val fullQueue = Queue(Set[Inhabitant](), Set[Inhabitant](), Set[Inhabitant](parent))
    val ancestryOne = new Ancestry(fullQueue, 3)
    val ancestryTwo = new Ancestry(emptyQueue, 3)

    val inhabitantOne = new TestInhabitant
    val inhabitantTwo = new TestInhabitant

    ancestryOne.areRelated(ancestryTwo) should be(false)
    val newAncestry = ancestryOne.breed(ancestryTwo, inhabitantOne, inhabitantTwo)

    newAncestry.allAncestors.size should be(3)
    newAncestry.ancestryQueue.size should be(3)
    val directParents = newAncestry.ancestryQueue(2)
    val grandparents = newAncestry.ancestryQueue(1)
    val greatGrandparents = newAncestry.ancestryQueue(0)
    directParents.size should be(2)
    grandparents.size should be(1)
    greatGrandparents.size should be(0)
    assert(directParents.contains(inhabitantOne))
    assert(directParents.contains(inhabitantTwo))
    assert(grandparents.contains(parent))
  }


  "An Ancestry" should " work on two parents " in {
    val parentOne = new TestInhabitant
    val parentTwo = new TestInhabitant

    val queueOne = Queue(Set[Inhabitant](), Set[Inhabitant](), Set[Inhabitant](parentOne))
    val queueTwo = Queue(Set[Inhabitant](), Set[Inhabitant](), Set[Inhabitant](parentTwo))
    val ancestryOne = new Ancestry(queueOne, 3)
    val ancestryTwo = new Ancestry(queueTwo, 3)

    val inhabitantOne = new TestInhabitant
    val inhabitantTwo = new TestInhabitant

    ancestryOne.areRelated(ancestryTwo) should be(false)
    val newAncestry = ancestryOne.breed(ancestryTwo, inhabitantOne, inhabitantTwo)

    newAncestry.allAncestors.size should be(4)
    newAncestry.ancestryQueue.size should be(3)
    val directParents = newAncestry.ancestryQueue(2)
    val grandparents = newAncestry.ancestryQueue(1)
    val greatGrandparents = newAncestry.ancestryQueue(0)
    directParents.size should be(2)
    grandparents.size should be(2)
    greatGrandparents.size should be(0)
    assert(directParents.contains(inhabitantOne))
    assert(directParents.contains(inhabitantTwo))
    assert(grandparents.contains(parentOne))
  }

  "An Ancestry" should " get rid of great grandparents " in {
    val greatGrandparent = new TestInhabitant

    val emptyQueue = Queue[Set[Inhabitant]]()
    val fullQueue = Queue(Set[Inhabitant](greatGrandparent), Set[Inhabitant](), Set[Inhabitant]())
    val ancestryOne = new Ancestry(fullQueue, 3)
    val ancestryTwo = new Ancestry(emptyQueue, 3)

    val inhabitantOne = new TestInhabitant
    val inhabitantTwo = new TestInhabitant

    ancestryOne.areRelated(ancestryTwo) should be(false)
    val newAncestry = ancestryOne.breed(ancestryTwo, inhabitantOne, inhabitantTwo)

    newAncestry.allAncestors.size should be(2)
    newAncestry.ancestryQueue.size should be(3)
    val directParents = newAncestry.ancestryQueue(2)
    val grandparents = newAncestry.ancestryQueue(1)
    val greatGrandparents = newAncestry.ancestryQueue(0)
    directParents.size should be(2)
    grandparents.size should be(0)
    greatGrandparents.size should be(0)
    assert(directParents.contains(inhabitantOne))
    assert(directParents.contains(inhabitantTwo))
  }

  "An Ancestry" should " should work with a full compliment of ancestors " in {

    val inhabitantOneParents: immutable.Set[Inhabitant] = 1 to 2 map(x => new TestInhabitant) toSet
    val inhabitantOneGrandparents: immutable.Set[Inhabitant] = 1 to 4 map(x => new TestInhabitant) toSet
    val inhabitantOneGreatGrandparents: immutable.Set[Inhabitant] = 1 to 8 map(x => new TestInhabitant) toSet

    val inhabitantTwoParents: immutable.Set[Inhabitant] = 1 to 2 map(x => new TestInhabitant) toSet
    val inhabitantTwoGrandparents: immutable.Set[Inhabitant] = 1 to 4 map(x => new TestInhabitant) toSet
    val inhabitantTwoGreatGrandparents: immutable.Set[Inhabitant] = 1 to 8 map(x => new TestInhabitant) toSet

    val inhabitantOneQueue = Queue(inhabitantOneGreatGrandparents, inhabitantOneGrandparents, inhabitantOneParents)
    val inhabitantTwoQueue = Queue(inhabitantTwoGreatGrandparents, inhabitantTwoGrandparents, inhabitantTwoParents)
    val ancestryOne = new Ancestry(inhabitantOneQueue, 3)
    val ancestryTwo = new Ancestry(inhabitantTwoQueue, 3)

    val inhabitantOne = new TestInhabitant
    val inhabitantTwo = new TestInhabitant

    ancestryOne.areRelated(ancestryTwo) should be(false)
    val newAncestry = ancestryOne.breed(ancestryTwo, inhabitantOne, inhabitantTwo)

    newAncestry.allAncestors.size should be(14)
    newAncestry.ancestryQueue.size should be(3)

    val directParents = newAncestry.ancestryQueue(2)
    val grandparents = newAncestry.ancestryQueue(1)
    val greatGrandparents = newAncestry.ancestryQueue(0)
    directParents.size should be(2)
    grandparents.size should be(4)
    greatGrandparents.size should be(8)

    assert(directParents.contains(inhabitantOne))
    assert(directParents.contains(inhabitantTwo))

    inhabitantOneParents.foreach(x => assert(grandparents.contains(x)))
    inhabitantTwoParents.foreach(x => assert(grandparents.contains(x)))

    inhabitantOneGrandparents.foreach(x => assert(greatGrandparents.contains(x)))
    inhabitantTwoGrandparents.foreach(x => assert(greatGrandparents.contains(x)))
  }

  "An Ancestry" should " should return true for areRelated if parents are related " in {
    val parent = new TestInhabitant

    val queueOne = Queue(Set[Inhabitant](), Set[Inhabitant](), Set[Inhabitant](parent))
    val queueTwo = Queue(Set[Inhabitant](), Set[Inhabitant](), Set[Inhabitant](parent))
    val ancestryOne = new Ancestry(queueOne, 3)
    val ancestryTwo = new Ancestry(queueTwo, 3)

    ancestryOne.areRelated(ancestryTwo) should be(true)
  }

  "An Ancestry" should " should return true for areRelated if multiple parents are related " in {
    val parentOne = new TestInhabitant
    val parentTwo = new TestInhabitant

    val queueOne = Queue(Set[Inhabitant](), Set[Inhabitant](), Set[Inhabitant](parentOne,parentTwo))
    val queueTwo = Queue(Set[Inhabitant](), Set[Inhabitant](), Set[Inhabitant](parentOne,parentTwo))
    val ancestryOne = new Ancestry(queueOne, 3)
    val ancestryTwo = new Ancestry(queueTwo, 3)

    ancestryOne.areRelated(ancestryTwo) should be(true)
  }

  "An Ancestry" should " should return true for areRelated if GrandParents are related " in {
    val grandParent = new TestInhabitant

    val queueOne = Queue(Set[Inhabitant](), Set[Inhabitant](grandParent), Set[Inhabitant]())
    val queueTwo = Queue(Set[Inhabitant](), Set[Inhabitant](grandParent), Set[Inhabitant]())
    val ancestryOne = new Ancestry(queueOne, 3)
    val ancestryTwo = new Ancestry(queueTwo, 3)

    ancestryOne.areRelated(ancestryTwo) should be(true)
  }

  "An Ancestry" should " should return true for areRelated if Great GrandParents are related " in {
    val greatGrandparents = new TestInhabitant

    val queueOne = Queue(Set[Inhabitant](greatGrandparents), Set[Inhabitant](), Set[Inhabitant]())
    val queueTwo = Queue(Set[Inhabitant](greatGrandparents), Set[Inhabitant](), Set[Inhabitant]())
    val ancestryOne = new Ancestry(queueOne, 3)
    val ancestryTwo = new Ancestry(queueTwo, 3)

    ancestryOne.areRelated(ancestryTwo) should be(true)
  }

}
