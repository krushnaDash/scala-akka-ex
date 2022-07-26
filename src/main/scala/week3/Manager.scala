package com.krushna
package week3

import akka.actor.{Actor, OneForOneStrategy}
import scala.concurrent.duration._


class Manager extends Actor {
  override def receive: Receive = ???

  /*override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1.minutes) {

case _: ArithmeticException => Resume
  case _: NullPointerException => Restart
  case _: IllegalArgumentException => Stop
  case _: Exception => Escalate
  }*/


}

