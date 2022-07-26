package com.krushna
package week2

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.LoggingReceive

class BankAcccount extends Actor{
  import BankAcccount._
  override def receive: Receive = LoggingReceive {
    case Deposit(amount) =>
      balance+=amount
      sender!Done
    case Withdraw(amount)=>
      if(amount<= balance) {
        balance-=amount
        sender!Done
      }else{
        sender ! Failed
      }
    case _ => sender !  Failed
  }
  var balance=0
}

object BankAcccount{
  case class Deposit(amount: Int){
    require(amount>0)
  }
  case class Withdraw(amount: Int){
    require(amount>0)
  }
  case object Done
  case object Failed
}

class WireTransfer extends  Actor{
  import  WireTransfer._
  override def receive: Receive = LoggingReceive{
    case Transfer(from,to,amount) =>
      from ! BankAcccount.Withdraw(amount)
      context.become(awitFrom(to,amount,sender))
  }
  def awitFrom(to: ActorRef, amount: Int, customer:ActorRef): Receive = LoggingReceive{
    case BankAcccount.Done =>
      to ! BankAcccount.Deposit(amount)
      context.become(awitTo(customer))
  }
  def awitTo(customer: ActorRef): Receive = LoggingReceive{
    case BankAcccount.Done =>
      customer ! Done
      context.stop(self)
  }
}
object WireTransfer{
  case class Transfer(from: ActorRef, to: ActorRef, amount: Int){
    require(amount>0)
  }
  case object Done
  case object Failed
}

class TransferMain extends Actor {
  val accountA =context.actorOf(Props[BankAcccount], "AccountA")
  val accountB =context.actorOf(Props[BankAcccount], "AccountA=B")
  accountA ! BankAcccount.Deposit(100)

  override def receive: Receive = LoggingReceive{
    case BankAcccount.Done =>
      println("Deposit of 100 Done")
      transfer(150)
  }
  def transfer(amount : Int) : Unit={
    val transferActor =context.actorOf(Props[WireTransfer], "transferActor")
    transferActor ! WireTransfer.Transfer(accountA,accountB,amount)
    context.become(LoggingReceive {
      case WireTransfer.Done =>
        println("Success")
        context.stop(self)
      case WireTransfer.Failed =>
        println("Insufficient Balance")
        context.stop(self)
    })
  }
}
object mainApp extends App{
  val system = ActorSystem("HelloSystem")
  val transferobj=system.actorOf(Props[TransferMain],"TransferMain")
}