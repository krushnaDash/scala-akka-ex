package com.krushna
package week2

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props, ReceiveTimeout, Status}
import com.krushna.week2.Controller.{Check, Result}
import com.krushna.week2.Getter.{Abort, Done}
import com.ning.http.client.AsyncHttpClient

import scala.concurrent.duration._
import scala.jdk.CollectionConverters._
import java.util.concurrent.Executor
import scala.concurrent.{Future, Promise}
import org.jsoup.Jsoup

import scala.util.{Failure, Success}

object mainCurlApp extends App{
  val system = ActorSystem("CurlUrl")
  println("Start of App")
  val transferobj=system.actorOf(Props[Main],"TransferMain")
  /* implicit val exec = system.dispatcher
  WebCrul.get("https://www.google.co.in/").onComplete({
    case Failure(exception) => println(exception)
    case Success(value) => println(value)
  })*/
}

class Main extends Actor{
  val receptionist = context.actorOf(Props[Receptionist], "receptionist")
  receptionist ! Receptionist.Get("https://www.google.co.in/")

  // we can send multiple request
  receptionist ! Receptionist.Get("https://www.google.co.in/1")

  context.setReceiveTimeout(320.second)

  override def receive: Receive = {
    case Result(set) =>
      println(set.toVector.sorted.mkString("result for the  "))
      println(set)
    case Receptionist.Failed(url) =>
      println(s"failed $url")
    case ReceiveTimeout =>
      println("Timeout happen >>>")
      context.stop(self)
  }

  override def postStop(): Unit = {
    WebCrul.shutDown()
  }
}


class Receptionist extends Actor {

  import Receptionist._

  override def receive = waiting

  val waiting: Receive = {
    case Get(url) => context.become(runNext(Vector(Job(sender, url))))
  }

  def running(queue: Vector[Job]): Receive = {
    case Controller.Result(links) =>
      val job = queue.head
      job.client ! Result(links)
      context.stop(sender)
      context.become(runNext(queue.tail))

    case Get(url) =>
      context.become(enqueueJob(queue, Job(sender, url)))
  }

  var requestNum = 0;

  def runNext(queue: Vector[Job]): Receive = {
    requestNum += 1
    if (queue.isEmpty) waiting
    else {
      val controler = context.actorOf(Props(new Controller()), "c" + requestNum)
      controler ! Controller.Check(queue.head.url, 2)
      running(queue)
    }
  }

  def enqueueJob(queue: Vector[Job], job: Job): Receive = {
    if (queue.size > 3) {
      sender ! Failed(job.url)
      running(queue)
    } else (running(queue :+ job))
  }
}

object Receptionist {
  case class Get(url: String)

  case class Job(client: ActorRef, url: String)

  case class Failed(url: String)
}



class Controller extends Actor with ActorLogging {
  var cahce = Set.empty[String]
  var childrenActor = Set.empty[ActorRef]
  // this timeout is reset by every received message
  context.setReceiveTimeout(90.second)

  override def receive: Receive = {
    case Check(link, depth) =>
      log.debug("{} checking with depth {}", link, depth)
      if (!cahce.contains(link) && depth > 0) {
        childrenActor += context.actorOf(Props(new Getter(link, depth - 1)))
        cahce += link
      }
    case Getter.Done =>
      childrenActor -= sender
      context.stop(sender)
      if (childrenActor.isEmpty) context.parent ! Result(cahce)

    case ReceiveTimeout =>
      childrenActor.foreach(_ ! Getter.Abort)
  }

}

object Controller {
  case class Check(link: String, depth: Int)
  case class Result(link: Set[String])
}

class Getter(url: String, depth: Int) extends Actor {
  implicit val exec = context.dispatcher
  val future = WebCrul.get(url)
  // future.pipeTo
  future.onComplete {
    case Success(value) => self ! value
    case Failure(exception) => self ! exception
  }

  override def receive: Receive = {
    case body: String =>
      for (link <- WebCrul.findLinks(body)) {
        context.parent ! Controller.Check(link, depth)
      }
      stop()
    case Abort => stop()
    case _: Status.Failure => stop()
  }

  def stop(): Unit = {
    context.parent ! Done
    context.stop(self)
  }
}

object Getter {
  case class Done()

  case class Abort()
}

object WebCrul {
  val client = new AsyncHttpClient()
  def get(url: String)(implicit exec: Executor): Future[String] = {
    println(s"Hitting to URL -> $url")
    val f = client.prepareGet(url).execute()
    val p = Promise[String]()
    f.addListener(new Runnable {
      override def run(): Unit = {
        val res = f.get();
        if (res.getStatusCode < 400)
          p.success(res.getResponseBody())
        else
          p.failure(new RuntimeException(res.getStatusCode + "Error"))
      }
    }, exec)
    p.future
  }

  def findLinks(body: String): Iterator[String] = {
    val document = Jsoup.parse(body)
    val links = document.select("a[href]")
    for (link <- links.iterator().asScala)
      yield link.absUrl("href")
  }

  def shutDown(): Unit = {
    client.close()
  }
}
