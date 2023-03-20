package com.krushna
package week5

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem}

/**
 * Akka Persistence
 *
 * https://livebook.manning.com/book/akka-in-action/chapter-15/
 */
object AkkaPersistence {
 sealed trait  Ledger

 case class Debit(account:String, amount: BigDecimal, replyTo:ActorRef[Result]) extends Ledger
  case class Credit(account:String, amount: BigDecimal, replyTo:ActorRef[Result]) extends Ledger
  case class Result() extends Ledger

  def main(args: Array[String]): Unit = {

  }

}
