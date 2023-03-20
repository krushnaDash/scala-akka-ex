package com.krushna
package persistence

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.testkit.{ImplicitSender, TestProbe}
import com.krushna.persistence2
import com.krushna.persistence2.{CmdAdd, CmdClear, CmdMultiply}
import com.typesafe.config.ConfigFactory


class AkkaPerssistenceCalculatorTest extends munit.FunSuite{
  val config = ConfigFactory.load("persistance")
  implicit val system = ActorSystem("EventStream", config)
  test(name = "The Calculator should recover last known result after crash") {
    val calc = system.actorOf(Props[Calculator],Calculator.name)
    val p= TestProbe()
    p.send(calc,CmdClear)
    p.send(calc,CmdAdd(5))
    p.send(calc,CmdMultiply(3))
    //p.send(calc,_root_.GetResult)
    p.expectMsg(persistence2.StateCalculationResult(15))
    // kill the persistence calculator
    p.watch(calc)
    system.stop(calc)
    p.expectTerminated(calc)

    val calcResurrected = system.actorOf(Props[Calculator], Calculator.name)
    p.send(calcResurrected,GetResult)
    p.expectMsg(persistence2.StateCalculationResult(15))
  }
}
