package com.krushna
package week4

import akka.actor.{Actor, ActorIdentity, ActorPath, ActorRef, ActorSystem, Identify, Props, ProviderSelection}
import com.krushna.week2.Toggle

/**
 * To resolve an actor from a path
 */
class ActorResolver extends Actor {
  import ActorResolver._
  override def receive: Receive = {

    case Resolve(path) =>
      // select an actor with the path with default message Identify, which all actor handle and reply with their identity
      context.actorSelection(path) ! Identify((path, sender))

    case ActorIdentity((path: ActorPath, client: ActorRef), Some(actorRef: ActorRef)) =>
      client ! ressolved(path, actorRef)

    case ActorIdentity((path: ActorPath, client: ActorRef), None) =>
      client ! NotRessolved(path)

  }

}

object ActorResolver {


  case class Resolve(actorPath: ActorPath)

  case class ressolved(actorPath: ActorPath, actorRef: ActorRef)

  case class NotRessolved(actorPath: ActorPath)
}

object TestActorResolver extends  App{

  implicit val togelSystem=ActorSystem("togelSystem")
  val toggle=togelSystem.actorOf(Props[Toggle],"ToggelAcotr")
  val path=toggle.path
  val actorResolver=togelSystem.actorOf(Props[ActorResolver])
  actorResolver ! ActorResolver.Resolve(path)
}