package com.krushna
package week5

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors


object EmotionalFunctionalActor {

  trait SimpleThing

  object EatChocolate extends SimpleThing

  object WashDishes extends SimpleThing

  object LearnAkka extends SimpleThing

  object Stop extends SimpleThing

  final case class Value(happiness: Int) extends SimpleThing

  final case class HowHappy(replyTo: ActorRef[SimpleThing]) extends SimpleThing

  /**
   * // the apply method can be called like a constructor, here it's returning a Behaviour
   *
   * @param happiness
   * @return
   */
  def apply(happiness: Int = 0): Behavior[SimpleThing] = Behaviors.receive { (context, message) =>
    message match {
      case EatChocolate =>
        context.log.info(s"($happiness) eating chocolate")
        EmotionalFunctionalActor(happiness + 1)

      case WashDishes =>
        context.log.info(s"($happiness) washing dishes, womp womp")
        EmotionalFunctionalActor(happiness - 2)

      case LearnAkka =>
        context.log.info(s"($happiness) Learning Akka, yes!!")
        EmotionalFunctionalActor(happiness + 100)

      case HowHappy(replyTo) =>
        replyTo ! Value(happiness)
        Behaviors.same
      case Stop =>
        context.log.info("Stoping the actor")
        Behaviors.stopped
      case _ =>
        context.log.warn("Received something i don't know")
        Behaviors.same
    }
  }

  def main(args: Array[String]): Unit = {
    // Here we can create the actor system like this and send message, we need to use the below
    // val actorSystem: ActorSystem[SimpleThing]=ActorSystem[SimpleThing](EmotionalFunctionalActor(1))
    val system = ActorSystem[Nothing](Behaviors.setup[Nothing] { ctx =>
      val messageRef = ctx.spawn(EmotionalFunctionalActor(1), "emotionalFunctionalActor")
      ctx.watch(messageRef) //sign death pact which means this will shutdown autometically once the  messageRef is shutdown
      messageRef ! EatChocolate
      messageRef ! LearnAkka
      messageRef ! WashDishes
      val checkHappyness = Behaviors.receiveMessage[SimpleThing] {
        case Value(happiness) =>
          println(s"Iam this much of happy $happiness")
          Behaviors.stopped[SimpleThing]
      }
      val replyTo = ctx.spawn(checkHappyness, "HappyNessChecker")
      // let check this latter how to do this
      messageRef ! HowHappy(replyTo)
      messageRef ! Stop
      Behaviors.empty
    }, "HelloWorldSystem")
    
  }
}