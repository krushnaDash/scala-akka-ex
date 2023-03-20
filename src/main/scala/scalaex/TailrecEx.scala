package com.krushna
package scalaex

import scala.annotation.tailrec

object TailrecEx extends App {

  println("sum of sqr from 1 to 4 is " + sum1(x => x * x) (1, 4))
  val x=mapReduce(x => x * x, (a, b) => a + b, 0)(1, 4)


  println("sum of sqr from 1 to 4 is with map reduce " + x)



  @tailrec
  final def gcd(a: Int, b: Int): Int = {
    if (b == 0)
      a
    else gcd(b, a % b)
  }


  /**
   * This is not tail recursive as the last call should the function call.
   * Here still the calculation is pending and hence multiple stack needed and it can't be
   * optimised
   *
   * @param n
   * @return
   */
  final def fact(n: Int): Int = {
    if (n == 0)
      1
    else
      n * fact(n - 1)
  }

  /**
   * optimized version
   */
  @tailrec
  final def factitr(n: Int, result: Int): Int = {
    if (n == 0)
      result
    else
      factitr(n - 1, result * n)
  }

  def factopti(n: Int) = factitr(n, 1)

  // tail recursive sum of differnt types
  // The function which will be applied to each number
  // like sumof Cubes from 1 to 10

  def sum(f: Int => Int, a: Int, b: Int): Int = {
    @tailrec
    def sumItr(a: Int, result: Int): Int = {
      if (a < b) {
        sumItr(a + 1, result + f(a))
      } else
        result
    }

    sumItr(a, 0)
  }

  // sum with simple syntax
  // this is called currying
  def sum1(f: Int => Int)(a: Int, b: Int): Int = {
    if (a > b)
      0
    else f(a) + sum1(f)(a + 1, b)
  }

  def product(f: Int=> Int)(a:Int, b:Int):Int={
    if(a>b)
      1
    else f(a)*product(f)(a+1,b)
  }

  def mapReduce(f:Int=>Int,combine:(Int,Int)=>Int, base:Int)(a:Int, b: Int): Unit ={
    if(a>b)
      base
    else mapReduce(f, combine, combine(f(a),base)) (a+1,b)
  }

}
