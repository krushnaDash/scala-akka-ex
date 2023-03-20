package com.krushna
package persistence2

import akka.actor.typed._
import akka.actor.typed.scaladsl.Behaviors
import akka.persistence.typed._
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior, RetentionCriteria}
import com.typesafe.config.ConfigFactory


// All the possible command
sealed trait CalcCommand

case object CmdClear extends CalcCommand

case class CmdAdd(value: Double) extends CalcCommand

case class CmdSubtract(value: Double) extends CalcCommand

case class CmdDivide(value: Double) extends CalcCommand

case class CmdMultiply(value: Double) extends CalcCommand

case object CmdPrintResult extends CalcCommand

final case class CmdGetResult(replyTo: ActorRef[StateCalculationResult]) extends CalcCommand

// All the possible events
sealed trait CalcEvent

case object EvntReset extends CalcEvent

case class EvntAdded(value: Double) extends CalcEvent

case class EvntSubtracted(value: Double) extends CalcEvent

case class EvntDivided(value: Double) extends CalcEvent

case class EvntMultiplied(value: Double) extends CalcEvent

// State
case class StateCalculationResult(result: Double = 0) {
  def reset = copy(result = 0)

  def add(value: Double) = copy(result = this.result + value)

  def subtract(value: Double) = copy(result = this.result - value)

  def divide(value: Double) = copy(result = this.result / value)

  def multiply(value: Double) = copy(result = this.result * value)
}


object AkkaSnapshottingEx {

  val updateState: (CalcEvent, StateCalculationResult) => StateCalculationResult = { (event, state) =>
    event match {
      case EvntReset => state.reset
      case EvntAdded(value) => state.add(value)
      case EvntSubtracted(value) => state.subtract(value)
      case EvntDivided(value) => state.divide(value)
      case EvntMultiplied(value) => state.multiply(value)
    }
  }

  val commandHandler: (StateCalculationResult, CalcCommand) => Effect[CalcEvent, StateCalculationResult] = { (state, command) =>
    command match {
      case CmdAdd(value) =>
        updateState(EvntAdded(value), state)
        Effect.persist(EvntAdded(value))
      case CmdSubtract(value) =>
        updateState(EvntSubtracted(value), state)
        Effect.persist(EvntSubtracted(value))
      case CmdDivide(value) =>
        updateState(EvntDivided(value), state)
        Effect.persist(EvntDivided(value))
      case CmdMultiply(value) =>
        updateState(EvntMultiplied(value), state)
        Effect.persist(EvntMultiplied(value))
      case CmdClear =>
        updateState(EvntReset, state)
        Effect.persist(EvntReset)
      case CmdGetResult(replyTo) =>
        replyTo ! state
        Effect.persist(EvntReset)
    }
  }

  val eventHandler: (StateCalculationResult, CalcEvent) => StateCalculationResult = { (state, event) =>
    event match {
      case event: CalcEvent => updateState(event, state)
    }
  }

  def apply(): Behavior[CalcCommand] = {
    Behaviors.setup { context =>
      EventSourcedBehavior[CalcCommand, CalcEvent, StateCalculationResult](
        persistenceId = PersistenceId.ofUniqueId("abc"),
        emptyState = StateCalculationResult(),
        commandHandler = (state, cmd) => commandHandler(state, cmd),
        eventHandler = (state, evt) => eventHandler(state, evt)
      ).withRetention(RetentionCriteria.snapshotEvery(numberOfEvents = 3, keepNSnapshots = 2).withDeleteEventsOnSnapshot)
        .receiveSignal { // optionally respond to signals
          case (state, _: SnapshotFailed) => println(s"snapshot failed for $state")
          case (state, _: DeleteSnapshotsFailed) => println(s"delete snapshot completed for $state")
        }
    }
  }

  def main(args: Array[String]): Unit = {
    SnapshotMetadata
    EventSourcedBehavior
    val config = ConfigFactory.load("persistance")

    val system = ActorSystem[Nothing](Behaviors.setup[Nothing] { ctx =>
      val messageRef = ctx.spawn(AkkaSnapshottingEx(), "AkkaSnapshotting")
      ctx.watch(messageRef)
      messageRef ! CmdClear
      messageRef ! CmdAdd(5)
      messageRef ! CmdMultiply(3)

      messageRef ! CmdClear
      messageRef ! CmdAdd(5)
      messageRef ! CmdMultiply(3)

      messageRef ! CmdClear
      messageRef ! CmdAdd(5)
      messageRef ! CmdMultiply(3)

      messageRef ! CmdClear
      messageRef ! CmdAdd(5)
      messageRef ! CmdMultiply(3)

      val getResult = Behaviors.receiveMessage[StateCalculationResult] {
        case StateCalculationResult(value) =>
          println(s"The calculation result $value")
          Behaviors.stopped[StateCalculationResult]
      }
      val replyTo = ctx.spawn(getResult, "HappyNessChecker")
      // let check this latter how to do this
      messageRef ! CmdGetResult(replyTo)
      Behaviors.empty
    }, "HelloWorldSystem",config)
  }
}
