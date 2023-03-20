package com.krushna
package scalaex


import org.apache.commons.math3.optim.InitialGuess

/**
 * To compute sqrt(x)
 * start with initial estimate y (lets pick y=1)
 * repeatedly improve the estimate by taking mean of y and x/y
 * example sqrt(2)
 *
 * Y=1 , mean of 1 & 2/1 =2         mean = 1.5
 * y=1.5 mean of 1.5 and 2/1.5 = 1.333  mean = 1.416
 * y=1.416 mean of 1.416 and 2/1.416=1.412 mean= 1.414
 * y=1.414 mean of 1.414 and 2/1.414=1.414 , mean =1.414
 *
 * ans=1.414
 */

object SqrtWithNewton extends App {
  println(sqrt(2))


  def sqrt(x: Double) = {
    def sqritr(guess: Double, x: Double): Double = {
      //https://docs.scala-lang.org/scala3/book/control-structures.html#the-ifthenelse-construct

      if (isGoodEnough(guess, x))
        guess
      else
        sqritr(improve(guess, x), x)
    }

    def improve(guess: Double, x: Double): Double = (guess + x / guess) / 2

    def isGoodEnough(guess: Double, x: Double): Boolean = guess * guess - x < x / 2000

    sqritr(x / 2, x)
  }
}
