package com.krushna
package week1

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object AsyncEx {
  def main(args: Array[String]): Unit = {
    val number = getNumber();
    println("start of main")
    number.onComplete {
      case Success(value) => println(value)
      case Failure(exception) => println(exception)
    }
  }

  def getNumber(): Future[Int] = {
    Thread.sleep(5000)
    println("getNumber")
    val number = Math.random() * 10
    Future(number.intValue())
  }
}
