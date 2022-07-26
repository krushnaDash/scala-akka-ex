package com.krushna
package week4

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}


case class Update(x: Int)
case object Get
case class Result(x: Int)
case class Sync(x: Int, timestamp: Long)
case object Hello


class DistributedStore extends Actor {
  var peers: List[ActorRef] = Nil
  var filed = 0
  var lastUpdate = System.currentTimeMillis()

  override def receive: Receive = {
    case Update(x) =>
      filed=x
      lastUpdate=System.currentTimeMillis()
      peers.foreach(_ ! Sync(filed,lastUpdate))

    case Get => sender ! Result(filed)

    case Sync(x, l)  if l > lastUpdate =>
        filed=x;
        lastUpdate=l

    case Hello =>
      peers ::=sender
      sender ! Sync(filed,lastUpdate)
  }
}


class ActorPrintResult extends  Actor {
  override def  receive ={
    case Result(x)=>
      println(s"Value of X IS ${x} from ${sender.path.name}")
  }
}


object EventualConsistency extends  App {
 implicit  val system= ActorSystem ("EventualConsistency-Demo")
  implicit val sender=system.actorOf(Props[ActorPrintResult]);

  val a=system.actorOf(Props[DistributedStore], "A");
  val b=system.actorOf(Props[DistributedStore], "B");

  a ! Get
  b ! Update(10)
  b ! Get
  b ! Get
  b.tell(Hello,a)
  a ! Get
  a ! Get
  a ! Get

  a ! Get
  a ! Get
  a ! Get

  a ! Get
  a ! Get
  a ! Get

  a ! Get
  a ! Get
  a ! Get


  system.terminate()

}

class AuditTrail(actorRef: ActorRef) extends Actor with ActorLogging{
  override def receive: Receive = {
    case msg =>
      log.info("sent {} to {}", msg,actorRef )
      actorRef forward msg
  }
}