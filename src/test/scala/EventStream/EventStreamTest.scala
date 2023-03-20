package com.krushna
package EventStream

import akka.actor.{ActorSystem, DeadLetter, PoisonPill, Props}
import akka.testkit.TestActors.EchoActor
import akka.testkit.TestProbe

import scala.concurrent.duration._
import scala.concurrent.duration.DurationInt

/**
 * Akka event Stream example
 *
 * Akka actor can subscribe to an event stream and all the actor recive an message when an event is published
 */
class EventStreamTest extends munit.FunSuite {
  implicit val system = ActorSystem("EventStream")

  test(name = "check Akka event stream") {
    val subcriber1 = TestProbe()
    val subcriber2 = TestProbe()
    val msg = "Test message"

    // Subscribe to event stream by both the actor
    system.eventStream.subscribe(subcriber1.ref, classOf[String])
    system.eventStream.subscribe(subcriber2.ref, classOf[String])
    // Push a message to event stream
    system.eventStream.publish(msg)
    subcriber1.expectMsg(msg)
    subcriber2.expectMsg(msg)

    //
    system.eventStream.unsubscribe(subcriber2.ref)
    system.eventStream.publish(msg)
    subcriber2.expectNoMessage(3.seconds)
  }

  test(name = "DeadLetter event") {
    val echoActor = system.actorOf(Props[EchoActor], "echoActor")
    val deafLettersubcriber = TestProbe()
    // subscribe DeadLetter, akka by default post all message which can't be deliver to actor
    system.eventStream.subscribe(deafLettersubcriber.ref, classOf[DeadLetter])
    // kill the actor
    echoActor ! PoisonPill
    // Now send a message to echoActor
    val msg="Are you Alive Still"
    echoActor ! msg
    val dead=deafLettersubcriber.expectMsgType[DeadLetter]
    dead.message.equals(msg)
    println(dead.sender)
    println(dead.recipient)
  }

}
