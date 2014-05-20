
package com.mayorgraeme.evol

import com.mayorgraeme.evol.path.Dijik


object Main {

//
//    def getNextDiagLonger(x: (Int, Int), y: (Int, Int)) = {
//        if (x._1 == y._1 || x._2 == y._2) Some(2) else Some(3)
//    }

    def getNextDiagLonger(x: (Int, Int), y: (Int, Int)) = {
        if (y._1 < 5 || y._2 < 5) Some(100000) else Some(1)
    }


    def main(args: Array[String]): Unit = {
        val dijik = new Dijik;
        val dijikGetNextDiagLonger = dijik.getNext(getNextDiagLonger) _

        val max = (10, 10)
        val start = (0, 0)
        val end = (0, 10)
        val ret = dijikGetNextDiagLonger(max)(start)(end)
        println(max, start, end)
        println(ret)

        Range(0, max._1+1).foreach(x => {
            Range(0, max._2+1).foreach(y => {
                val coord = (x, y);
                print {
                    if (coord == start) {
                        'S'
                    } else if (coord == end) {
                        'E'
                    } else if (ret.contains((x, y))) {
                        '+'
                    } else {
                        '~'
                    }
                }
            })

            println
        }

        )
    }

}

