package com.krushna
package persistence2

import akka.actor.typed.ActorRef

// all possible command

sealed trait CartCommand

case class AddItem(itemId: String, qty: Int) extends CartCommand

case class RemoveItem(itemId: String, qty: Int) extends CartCommand

final case class CmdGetItems(replyTo: ActorRef[CartItemState]) extends CartCommand

// all possible events

sealed trait CartEvent

case class ItemAdded(itemId: String, qty: Int) extends CartEvent

case class ItemRemoved(itemId: String, qty: Int) extends CartEvent

final case class EvntGetItems(replyTo: ActorRef[CartItemState]) extends CartCommand

// State

case class CartItemState(items: Map[String, Int] = Map.empty) {
  //def reset = copy(items = Map.empty)

  //def add(itemId: String, qty: Int) = copy(items + (itemId, qty))

  //def remove(itemId: String, qty: Int) = copy(items - (itemId, qty))
}

class AkkaSnapshotWithMap {
  def main(args: Array[String]): Unit = {


    }

}
