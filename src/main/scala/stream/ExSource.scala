package com.krushna
package stream

import akka.stream._
import akka.stream.scaladsl._
import akka.{ Done, NotUsed }
import akka.actor.ActorSystem
import akka.util.ByteString
import scala.concurrent._
import scala.concurrent.duration._
import java.nio.file.Paths

object ExSource extends App {
 implicit val system:ActorSystem=ActorSystem("ExSource")

  val source: Source[Int, NotUsed] = Source(1 to 5)
  //val done: Future[Done]=source.runForeach(i => println(i))

  //implicit val ec = system.dispatcher
  //done.onComplete(_ => system.terminate())


  val factorials = source.scan(BigInt(1))((acc, next) => acc * next)

  factorials.runForeach(println(_))

}
