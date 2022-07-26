package com.krushna
package week2

import akka.actor.{Actor, ActorSystem, Props}
class Toggle extends Actor{
  override def receive: Receive = happy
  def happy: Receive ={
    case "How are you ? " =>
      sender ! "happy"
      context.become(sad)
  }
  def sad: Receive ={
    case "How are you ? " =>
      sender ! "sad"
      context.become(happy)
  }
}

