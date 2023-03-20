package com.krushna
package week5

import akka.actor.testkit.typed.scaladsl.TestInbox
import akka.actor.testkit.typed.scaladsl.BehaviorTestKit
import com.krushna.week5.EmotionalFunctionalActor.{EatChocolate, HowHappy}

class EmotionalFunctionalActorTest extends munit.FunSuite {
// This approach no need to wait, here we are testing the behaviours
  test("Happiness value should be increase by 1") {
    val testkit = BehaviorTestKit(EmotionalFunctionalActor(0))
    val testInbox = TestInbox[EmotionalFunctionalActor.SimpleThing]()
    testkit.run(EatChocolate)
    testkit.run(HowHappy(testInbox.ref))
    testInbox.expectMessage(EmotionalFunctionalActor.Value(1))
    testkit.run(EmotionalFunctionalActor.Stop)
    assertEquals(false,testkit.isAlive)
  }
}
