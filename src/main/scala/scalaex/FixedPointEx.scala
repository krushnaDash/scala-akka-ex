package com.krushna
package scalaex

import math.abs

object FixedPointEx extends App{
  val tolerance=0.0001
  println(sqrt(4))

  /**
   * sqrt(x) is number such that y*y=x
   * dividing y in both side we will get y=x/y
   * so we can say the sqrt(x) is fixed point function y=>x/y
   */
  def sqrt(x:Double) = fixedPoint(y => (y+x/y)/2)(1.0)


  /**
   * A number will called fixed point of a function if f(x) is x
   * for some function we can locate fixed point by iterating over an over like
   * x,f(x),f(f(x)), f(f(f(x))) .....
   * @param f
   * @param firstGuess
   * @return
   */
  def fixedPoint(f:Double=>Double)(firstGuess:Double):Double = {
    def iterate(guess:Double):Double= {
      val next=f(guess)
      if(isCloseEnough(guess,next))
        next
      else iterate(next)
    }
    iterate(firstGuess)
  }

  def isCloseEnough(x:Double,y:Double)= abs((x-y)/x) < tolerance

}
