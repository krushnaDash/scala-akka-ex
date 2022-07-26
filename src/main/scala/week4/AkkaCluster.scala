/* package com.krushna
package week4

import akka.actor.{Actor, ActorContext, ActorRef, Address, Deploy, Props, ReceiveTimeout, SupervisorStrategy, Terminated}
import akka.cluster.ClusterEvent.{MemberRemoved, MemberUp}
import akka.cluster.{Cluster, ClusterEvent}
import akka.remote.RemoteScope
import akka.remote.WireFormats.RemoteScope
import com.krushna.week2.{Controller, Receptionist}

import scala.concurrent.duration.DurationInt


class ClusterMain extends Actor {
  val cluster = Cluster(context.system)
  cluster.subscribe(self, classOf[ClusterEvent.MemberUp])
  cluster.join(cluster.selfAddress)

  override def receive: Receive = {
    case ClusterEvent.MemberUp(member) =>
      if (member.address != cluster.selfAddress) {
        // some one joined
      }

  }
  // this will start single cluster node on port 2552
}
// this config need akka.remote.netty.tcp.port=0

class ClusterWorker extends Actor {
  val cluster = Cluster(context.system)
  cluster.subscribe(self, classOf[ClusterEvent.MemberRemoved])
  val main = cluster.selfAddress.copy(port = Some(2552))
  cluster.join(main)

  override def receive: Receive = {
    case ClusterEvent.MemberRemoved(m, _) =>
      if (m.address == main) context.stop(self)
  }
}

class ClusterReceptionist extends Actor {
  val cluster = Cluster(context.system)
  cluster.subscribe(self, classOf[ClusterEvent.MemberRemoved])
  cluster.subscribe(self, classOf[ClusterEvent.MemberUp])

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }


  override def receive: Receive = awaitingMember

  val awaitingMember: Receive = {
    case current: ClusterEvent.CurrentClusterState =>
      val addresses = current.members.toVector.map(_.address)
      val notMe = addresses.filter(_ != cluster.selfAddress)
      if (notMe.nonEmpty) context.become(active(notMe))
    case MemberUp(member) if member.address != cluster.selfAddress =>
      context.become(active((Vector(member.address))))
    case "Get(Url)" => sender ! "failed URL, no nodes avaliable"
  }

  def active(addresses: Vector[Address]): Receive = {
    case MemberUp(member) if member.address != cluster.selfAddress =>
      context.become(active(addresses :+ member.address))
    case MemberRemoved(member, _) =>
      val next = addresses.filterNot(_ == member.address)
      if (next.isEmpty) context.become(awaitingMember)
      else
        context.become(active(next))
    case "Get(Url)" if context.children.size < addresses.size =>
      val client = sender
      //val address=pick(addresses)
      val address = addresses(1) // choose a random address here
      context.actorOf(Props(new Customer(client, "", address)), "Test")
  }


}

class AkkaCluster {

}

class Customer(client: ActorRef, url: String, node: Address) extends Actor {
  // this will take precedence for actor implicit variable , inseted of self as sender , all message will be send from it's parent
  implicit val s = context.parent

  override def receive: Receive = ({

    case ReceiveTimeout =>
      context.unwatch(controller)
      client ! Receptionist.Failed("controller timeout")
    case Terminated(_) =>
    client ! Receptionist.Failed("contorller died")

    case Controller.Result(links) =>
        context.unwatch(controller)
        //client ! Receptionist.Result(links)

  }: Receive).andThen(_ => context.stop(self))



  override val supervisorStrategy = SupervisorStrategy.stoppingStrategy
val props = Props[Controller].withDeploy(Deploy(scope = RemoteScope(node)))
  val controller = context.actorOf(props, "controller")
  context.watch(controller)

  context.setReceiveTimeout(5.seconds)
  controller ! Controller.Check(url, 2)
}
*/