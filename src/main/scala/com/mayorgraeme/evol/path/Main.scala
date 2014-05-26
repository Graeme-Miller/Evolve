
package com.mayorgraeme.evol.path


object Main {


    val max = (30, 30)
    val start = (0, 0)
    val end = (25, 25)

    val mountains = {
        Range(0, max._2 + 1).filter(_ != 5).map((_, 4)).
            toList++ Range(0, max._2 + 1).filter(_ != 9).map((_, 7)).
            toList++ Range(0, max._2 + 1).filter(_ != 10).map((_, 2))
    }



    //Range(0,max._1).foreach(x => Range(0, Max._2).filter(  ))

    def getNextDiagLonger(x: (Int, Int), y: (Int, Int)) = {
        if (mountains.contains((x)) || mountains.contains((y))) None else if (x._1 == y._1 || x._2 == y._2) Some(2) else Some(3)
    }

    //
    //    def getNextDiagLonger(x: (Int, Int), y: (Int, Int)) = {
    //        if (y._1 < 5 || y._2 < 5) Some(100000) else Some(1)
    //    }


    def main(args: Array[String]): Unit = {
        val dijikGetNextDiagLonger = Dijik.getNext(getNextDiagLonger) _


        val ret = dijikGetNextDiagLonger(max)(start)(end)
        println(max, start, end)
        println(ret)



        Range(0, max._1 + 1).foreach(x => {
            Range(0, max._2 + 1).foreach(y => {
                val coord = (x, y);
                print(" " + {
                    if (coord == start) {
                        'S'
                    } else if (coord == end) {
                        'E'
                    } else if (mountains.contains((x, y))) {
                        '^'
                    } else if (ret.contains((x, y))) {
                        '*'
                    } else {
                        '.'
                    }
                } + " ")
            })

            println
        }

        )
    }

}

