package com.krushna

object TestObject extends App{
  var testMap:Map[String,Int]=Map("One" -> 1, "Two" -> 2)
  testMap=testMap+ ("One" -> 11)
  println(testMap)

}
