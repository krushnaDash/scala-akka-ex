package com.krushna
package weatherEx

import java.lang.Thread
import scala.concurrent.Future

object FutuerEx2 extends App {

  /**
   * this is needed as Future.apply() method take an implicit parameter ExecutionContext
   */

  import scala.concurrent.ExecutionContext.Implicits.global

  val futureVale = Future {
    println("The execution will happen in a separate thread with name " + Thread.currentThread().getName)
    Thread.sleep(1000)
    10
  }
  println("This is main thread " + Thread.currentThread().getName)
}
