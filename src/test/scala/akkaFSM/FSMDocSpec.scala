package com.krushna
package akkaFSM

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

import scala.concurrent.duration._
import scala.collection.immutable
import akka.actor.testkit.typed.scaladsl.{LogCapturing, ScalaTestWithActorTestKit, TestProbe}
import org.scalatest.wordspec.AnyWordSpecLike

object FSMDocSpec {
  object Buncher {

    // data
    sealed trait Data

    case object Uninitialized extends Data

    final case class Todo(target: ActorRef[Batch], queue: immutable.Seq[Any]) extends Data

    final case class Batch(obj: immutable.Seq[Any])

    // FSM event becomes the type of the message Actor supports
    // possible events
    sealed trait Event

    final case class SetTarget(ref: ActorRef[Batch]) extends Event

    final case class Queue(obj: Any) extends Event

    case object Flush extends Event

    private case object Timeout extends Event


    // initial state
    def apply(): Behavior[Event] = idle(Uninitialized)

    private def idle(data: Data): Behavior[Event] = Behaviors.receiveMessage[Event] { message =>
      (message, data) match {
        case (SetTarget(ref), Uninitialized) =>
          idle(Todo(ref, Vector.empty))
        case (Queue(obj), t@Todo(_, v)) =>
          active(t.copy(queue = v :+ obj))
        case _ =>
          Behaviors.unhandled
      }
    }

    private def active(data: Todo): Behavior[Event] =
      Behaviors.withTimers[Event] { timers =>
        // instead of FSM state timeout
        timers.startSingleTimer(Timeout, 1.second)
        Behaviors.receiveMessagePartial {
          case Flush | Timeout =>
            data.target ! Batch(data.queue)
            idle(data.copy(queue = Vector.empty))
          case Queue(obj) =>
            active(data.copy(queue = data.queue :+ obj))
        }

      }
  }
}

class FSMDocSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike with LogCapturing {

  import FSMDocSpec._

  "FSMDocSpec" must {
    "work" in {
      val buncher = spawn(Buncher())
      val probe = TestProbe[Buncher.Batch]()
      buncher ! Buncher.SetTarget(probe.ref)
      buncher ! Buncher.Queue(42)
      buncher ! Buncher.Queue(43)
      probe.expectMessage(Buncher.Batch(immutable.Seq(42, 43)))
      buncher ! Buncher.Queue(44)
      buncher ! Buncher.Flush
      buncher ! Buncher.Queue(45)
      probe.expectMessage(Buncher.Batch(immutable.Seq(44)))
      probe.expectMessage(Buncher.Batch(immutable.Seq(45)))

    }
  }
}
