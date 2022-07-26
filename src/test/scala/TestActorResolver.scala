package com.krushna

import week2.Toggle

import akka.actor.{ActorPath, ActorRef, ActorSystem, Props}
import akka.testkit.TestProbe
import com.krushna.week4.ActorResolver

import scala.concurrent.duration._

class TestActorResolver extends munit.FunSuite {

  test("Toogle Actor Test") {
    implicit val togelSystem=ActorSystem("togelSystem")
    val toggle=togelSystem.actorOf(Props[Toggle],"ToggelAcotr")
    val path=toggle.path
    val actorResolver=togelSystem.actorOf(Props[ActorResolver])
    actorResolver ! ActorResolver.Resolve(path)
    val p= TestProbe()
    p.expectMsg(60.second,ActorResolver.ressolved(path, toggle))
  }
}
