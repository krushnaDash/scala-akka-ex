package com.krushna
package weatherEx

import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object FutureEx {
  def main(args: Array[String]): Unit = {
  println("start")
  val data=new WeatherForecastService(new HttpClient())
   val w =data.forecast("29/04/2022")
    w.onComplete{
      case Success(value) => println(value)
      case Failure(exception) => println(exception)
    }
  }
}
