
package com.mayorgraeme.evol


import scala.concurrent.duration._
import com.mayorgraeme.evol.Messages._




object Main {
 
  
  def main(args: Array[String]): Unit = {    
    
    val lg = new LocationGenerator(50,100)
    
    lg.map.foreach{ x => {
        x.foreach{ y => {
            print(" "+y)
          }
        }
        println
      }
    }
  }
}

