package com.krushna
package persistence

import akka.actor.{ActorLogging, ActorSystem, PoisonPill, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import com.typesafe.config.ConfigFactory


// All the possible command
sealed trait Command

case object Clear extends Command

case class Add(value: Double) extends Command

case class Subtract(value: Double) extends Command

case class Divide(value: Double) extends Command

case class Multiply(value: Double) extends Command

case object PrintResult extends Command

case object GetResult extends Command

// All the possible events
sealed trait Event

case object Reset extends Event

case class Added(value: Double) extends Event

case class Subtracted(value: Double) extends Event

case class Divided(value: Double) extends Event

case class Multiplied(value: Double) extends Event

case class CalculationResult(result: Double = 0) {
  def reset = copy(result = 0)

  def add(value: Double) = copy(result = this.result + value)

  def subtract(value: Double) = copy(result = this.result - value)

  def divide(value: Double) = copy(result = this.result / value)

  def multiply(value: Double) = copy(result = this.result * value)
}


class Calculator extends PersistentActor with ActorLogging {
  override def receiveCommand: Receive = {
    // event to persist and handler for each persisted event
    case Add(value) => persist(Added(value))(updateState)
    case Subtracted(value) => persist(Subtracted(value))(updateState)
    case Divide(value) => persist(Divided(value))(updateState)
    case Multiply(value) => persist(Multiplied(value))(updateState)
    case PrintResult => println(s"The calculation final value ${state.result}")
    case Clear => persist(Reset)(updateState)
    case GetResult => sender ! state
  }

  override def receiveRecover: Receive = {
    case event: persistence2.CalcEvent => updateState
    case RecoveryCompleted => log.info("Calculator recovery completed")
  }

  def persistenceId = Calculator.name


  var state = CalculationResult()

  val updateState: Event => Unit = {
    case Reset => state = state.reset
    case Added(value) => state = state.add(value)
    case Subtracted(value) => state = state.subtract(value)
    case Divided(value) => state = state.divide(value)
    case Multiplied(value) => state = state.multiply(value)
  }

}

object Calculator {
  val name = "KrushnaCalc"
}

object PersistenceCalculater extends App {
  val config = ConfigFactory.load("persistance")
  val system = ActorSystem("PersistanceCalulator", config)
  val calculatorActor = system.actorOf(Props[Calculator])
  calculatorActor ! persistence2.CmdClear
  calculatorActor ! persistence2.CmdAdd(5)
  calculatorActor ! persistence2.CmdMultiply(3)
  calculatorActor ! persistence2.CmdPrintResult
}
