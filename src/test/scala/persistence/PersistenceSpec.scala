package com.krushna
package persistence

import java.io.File
import com.typesafe.config._

import scala.util._
import akka.actor._
import akka.persistence._
import akka.testkit.TestKit
import org.scalatest._
abstract class PersistenceSpec(system: ActorSystem) extends TestKit(system)
  {
  def this(name: String, config: Config) = this(ActorSystem(name, config))
  def killActors(actors: ActorRef*) = {
    actors.foreach { actor =>
      watch(actor)
      system.stop(actor)
      expectTerminated(actor)
    }
  }
}