package com.krushna
package persistence

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.EventSourcedBehavior


sealed trait Ledger

case class Debit(account: String, amount: BigDecimal, replyTo: ActorRef[Result]) extends Ledger

case class Credit(account: String, amount: BigDecimal, replyTo: ActorRef[Result]) extends Ledger

object MyPersistentBehavior {
  sealed trait Command

  sealed trait Event

  final case class State()

  def apply(): Behavior[Command] =
    EventSourcedBehavior[Command, Event, State](
      persistenceId = PersistenceId.ofUniqueId("abc"),
      emptyState = State(),
      commandHandler = (state, cmd) => throw new NotImplementedError("TODO: process the command & return an Effect"),
      eventHandler = (state, evt) => throw new NotImplementedError("TODO: process the event return the next state"))
}

object LedgerService extends App {

  ActorSystem(Behaviors.setup[Result] { ctx =>
    val transfer = ctx.spawn(MyPersistentBehavior(), "ledgerSystem")
    Behaviors.empty
  }
    , "pesrsistence")
}

case class Result()
