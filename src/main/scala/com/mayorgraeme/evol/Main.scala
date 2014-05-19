
package com.mayorgraeme.evol

import com.mayorgraeme.evol.path.Dijik


object Main {


    def main(args: Array[String]): Unit = {
        val dijik = new Dijik;

        val ret = dijik.getNext((x,y) => {Some(1)})((5, 5))((1,1))((3,3))
        println(ret)
    }
}

