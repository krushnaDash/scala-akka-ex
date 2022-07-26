package com.krushna

import week2.Toggle

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestProbe
import scala.concurrent.duration._

import scala.concurrent.duration.FiniteDuration

class TestActor extends munit.FunSuite {

  test("Toogle Actor Test") {
    implicit val togelSystem=ActorSystem("togelSystem")
    val toggle=togelSystem.actorOf(Props[Toggle],"ToggelAcotr")
    println(toggle)
    val p= TestProbe()
    p.send(toggle, "How are you ? ")
    p.expectMsg(60.second, "happy")
    p.send(toggle, "How are you ? ")
    p.expectMsg(5.second,"sad")
    p.send(toggle, "unkonwn")
    p.expectNoMessage(1.second)
    togelSystem.terminate()
  }
}
