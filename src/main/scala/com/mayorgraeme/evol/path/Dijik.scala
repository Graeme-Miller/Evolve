package com.mayorgraeme.evol.path

import scala.collection.mutable
import org.jgrapht.util.{FibonacciHeapNode, FibonacciHeap}
;


/**
 * Created by gmiller on 16/05/14.
 */

object Dijik {

    type Coord = (Int, Int);
    val maxCoord = 1000

    val coordMap: Map[Coord, List[Coord]] = Range(0, maxCoord+1).map(x => Range(0,maxCoord+1).map(y => (x,y))).flatten.map(a => (a, getSourounding(a, (maxCoord,maxCoord)))).toMap


    def getSourounding(coord: Coord, max:Coord): List[Coord] = {
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

    def getNext(distanceFunc: (Coord, Coord) => Option[Int])(max: Coord)(start: Coord)(end: Coord): List[Coord] = {

        val previous = new mutable.HashMap[Coord, Coord]
        val distance = new mutable.HashMap[Coord, FibonacciHeapNode[Coord]]
        val priorityQueue = new FibonacciHeap[Coord]()

        def decreaseDistance(coord: Coord, newDistance:Double){
            val node = distance(coord)
            priorityQueue.decreaseKey(node, newDistance)
        }


        def decreaseDistanceWithPrevious(coord: Coord, dist: Double, prev: Coord) = {
            decreaseDistance(coord, dist)
            previous.put(coord, prev)
        }



        Range(0, max._1 + 1).foreach(x => Range(0, max._2 + 1).foreach(y => {
            val newCoord = (x, y)
            val newNode = new FibonacciHeapNode[Coord](newCoord, Double.PositiveInfinity)
            priorityQueue.insert(newNode, newNode.getKey)
            distance.put(newCoord, newNode)
        }))

        decreaseDistance(start, 0)


        var prev: Coord = null
        do {
            val prevNode = priorityQueue.removeMin()
            prev = prevNode.getData
            val distanceToPrevious = prevNode.getKey

            coordMap(prev) foreach { current =>
                distanceFunc(prev, current) match {
                    case Some(distancePrevToCurr) => {


                        val distanceToCurrViaPrev = distanceToPrevious + distancePrevToCurr

                        distance.get(current) match {
                            case Some(distanceToCurrNode) => {
                                val distanceToCurr = distanceToCurrNode.getKey
                                if (distanceToCurr > distanceToCurrViaPrev) {
                                    decreaseDistanceWithPrevious(current, distanceToCurrViaPrev, prev)
                                }
                            }
                            case None =>
                        }
                    }
                    case None =>

                }
            }

            distance.remove(prev)

        } while (!priorityQueue.isEmpty() && prev != end)




        val listBuffer = new mutable.ListBuffer[Coord];

        var curr = end
        while (previous.contains(curr)) {
            listBuffer.append(curr)
            curr = previous(curr)
        }

        listBuffer.toList
    }

}