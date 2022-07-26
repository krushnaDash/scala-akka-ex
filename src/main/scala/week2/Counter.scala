package com.krushna
package week2

import akka.AkkaException
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.sun.tools.javac.main.Option

class Counter extends Actor{
  var count=0
  override def receive: Receive = {
    case "incr" => count+=1
    case ("get", customer: ActorRef) => customer ! count
    case "find" => sender ! count
  }
}

class CounterMain extends Actor{
  val counter=context.actorOf(Props[Counter],"counter")
  counter ! "incr"
  counter ! "incr"
  counter ! "incr"
  counter ! "get"
  counter ! "incr"
  counter ! "find"

  override def receive: Receive = {
    case count:Int =>
      println(s"value of current count is $count")
  }
}
object CounterTest extends  App {
  val system=ActorSystem("counterSystem")
  val counterMainActor=system.actorOf(Props[CounterMain],"CounterMain")
}



