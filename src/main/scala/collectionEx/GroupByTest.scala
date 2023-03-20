package com.krushna
package collectionEx

object GroupByTest extends App {

  val ages=List(2,52,12,34,45,56,60,12,67,79,30,22,19,34)
  val grouped=ages.groupBy( age => if(age >45) "old" else if (age >18 && age <=45) "adult" else "child" )
  println(grouped)

}
