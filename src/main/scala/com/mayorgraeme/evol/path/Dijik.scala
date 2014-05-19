package com.mayorgraeme.evol.path

import scala.collection.mutable
import org.jgrapht.util.FibonacciHeap


/**
 * Created by gmiller on 16/05/14.
 */
class Dijik {

    type Coord = (Int, Int);

    def getNext(distanceFunc: (Coord, Coord) => Option[Int])(max: Coord)(start: Coord)(end: Coord): List[Coord] = {

        val previous = new mutable.HashMap[Coord, Coord]
        val distance = new mutable.HashMap[Coord, Int]
        val priorityQueue = new mutable.PriorityQueue[Coord]()(Ordering.by(g =>
            distance.get(g) match {
                case Some(x) => -x
                case None => Double.NegativeInfinity
            }
        ))

        val fibHeap = new FibonacciHeap[Coord]


        def addOrUpdateWithPrevious(coord: Coord, dist: Int, prev: Coord) = {
            val oldVal = distance.put(coord, dist)
            previous.put(coord, prev)

//            println(coord, dist, prev)
//            println(distance)
        }

        def getSourounding(coord: Coord): List[Coord] = {
            val coordOneMinusOne = coord._1 - 1
            val coordOnePlusOne = coord._1 + 1
            val coordTwoMinusOne = coord._2 - 1
            val coordTwoPlusOne = coord._2 + 1

            val listBuffer = new mutable.ListBuffer[Coord];

            if (coordOneMinusOne >= 0 && coordTwoMinusOne >= 0) listBuffer append ((coordOneMinusOne, coordTwoMinusOne))
            if (coordOneMinusOne >= 0) listBuffer append ((coordOneMinusOne, coord._2))
            if (coordOneMinusOne >= 0 && coordTwoPlusOne <= max._2) listBuffer append ((coordOneMinusOne, coordTwoPlusOne))

            if (coordOnePlusOne <= max._1 && coordTwoMinusOne >= 0) listBuffer append ((coordOnePlusOne, coordTwoMinusOne))
            if (coordOnePlusOne <= max._1) listBuffer append ((coordOnePlusOne, coord._2))
            if (coordOnePlusOne <= max._1 && coordTwoPlusOne <= max._2) listBuffer append ((coordOnePlusOne, coordTwoPlusOne))


            if (coordTwoMinusOne >= 0) listBuffer append ((coord._1, coordTwoMinusOne))
            if (coordTwoPlusOne <= max._2) listBuffer append ((coord._1, coordTwoPlusOne))

            listBuffer.toList;
        }

        distance.put(start, 0)
        Range(0, max._1 + 1).foreach(x => Range(0, max._2 + 1).foreach(y => priorityQueue.enqueue((x, y))))

        priorityQueue.takeWhile{ previous =>
            println(previous, distance)
            if(priorityQueue.isEmpty || !distance.contains(previous) || previous == end){
                false
            }else {
                distance.get(previous) match {
                    case Some(distanceToPrevious) => {
                        getSourounding(previous) foreach { current =>
                            distanceFunc(previous, current) match {
                                case Some(distancePrevToCurr) => {
                                    val distanceToCurrViaPrev = distanceToPrevious + distancePrevToCurr
                                    distance.get(current) match {
                                        case Some(distanceToCurr) => {
                                            if (distanceToCurr < distanceToCurrViaPrev) {
                                                addOrUpdateWithPrevious(current, distanceToCurrViaPrev, previous)
                                            }
                                        }
                                        case None => addOrUpdateWithPrevious(current, distanceToCurrViaPrev, previous)
                                    }
                                }
                            }
                        }
                    }
                    case None => Unit //maybe return here? If there are no distances to head
                }
                true
            }
        }

        println("*****************")
        mutable.HashMap
        fibHeap.insert((1,2), 3)
        fibHeap.insert((2,2), 4)
        fibHeap.insert((2,3), 5)
        println(fibHeap.removeMin())
        println(fibHeap.removeMin())
        println(fibHeap.removeMin())

        fibHeap.insert((1,2), 3)
        fibHeap.insert((2,2), 4)
        fibHeap.insert((2,3), 5)
        fibHeap.decreaseKey((2,3),1)
        println(fibHeap.removeMin())
        println(fibHeap.removeMin())
        println(fibHeap.removeMin())

        println("*****************")



        val listBuffer = new mutable.ListBuffer[Coord];
        println(previous)
        println(end)
        var curr = end
        while(previous.contains(curr)){
            listBuffer.append(curr)
            curr = previous(curr)
        }

        listBuffer.toList
    }

}