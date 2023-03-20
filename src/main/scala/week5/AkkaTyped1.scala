package com.krushna
package week5

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.javadsl.Behaviors

trait Message;

case class PrintMessage(msg: String) extends Message

case object Stop extends Message

object AkkaTyped1 extends App {
  val messageBehavior: Behavior[Message] = Behaviors.receiveMessage[Message]({
    case PrintMessage(p) =>
      println(s"the message is ${p}")
      Behaviors.same
    case Stop =>
      println("shutting down")
      Behaviors.stopped[Message]
  })

  // we will create the actor system from the behaviour that only initilize the actor system and become inactive after that.
  val system=ActorSystem[Nothing](Behaviors.setup[Nothing] { ctx =>
    val messageRef = ctx.spawn(messageBehavior, "messager")
    ctx.watch(messageRef) //sign death pact which means this will shutdown autometically once the  messageRef is shutdown
    messageRef ! PrintMessage("Hello World")
    messageRef ! Stop
    Behaviors.empty
  }, "HelloWorldSystem")

}
