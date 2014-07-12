package com.mayorgraeme.evol.util

object SexUtil {

  def getOpositeSex(value: Char): Char = {
    value match {
      case 'M' => 'F'
      case 'F' => 'M'
    }
  }
}
