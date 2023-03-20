package com.krushna
package week5

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.javadsl.Behaviors

/**
 * In Akka typed we do not create an Actor, we create a Behavior.
 *
 *
 * Behaviors.recciveMessage : factory method to create a Behavior
 * args : type
 * function : to compute the next behaviour
 */
object AkkaTyped extends App {

  val sayHello: Behavior[String] = Behaviors.receiveMessage[String](message => {
    println(s"The message is $message")
    Behaviors.stopped[String]
  })

  val actorSystem: ActorSystem[String]= ActorSystem(sayHello,"HelloWorldActor")
  actorSystem ! "Hello World"
}
