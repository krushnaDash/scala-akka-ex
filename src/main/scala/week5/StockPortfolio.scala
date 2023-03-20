package com.krushna
package week5

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import com.krushna.week5.StockPortfolio.PortfolioActor.PortfolioCommand

import java.util.UUID
// may want to complete : https://www.baeldung.com/scala/typed-akka
// https://github.com/Baeldung/scala-tutorials/blob/master/scala-akka/src/main/scala/com/baeldung/scala/akka/typed/PortfolioApplication.scala
object StockPortfolio {

  object Bank {
    final case class CreatePortfolio(clinet: ActorRef[PortfolioCreated])

    final case class PortfolioCreated(portfolio: ActorRef[PortfolioCommand])

    def apply(): Behavior[CreatePortfolio] = Behaviors.receive {
      (context, message) =>
        val replyTo = context.spawn(PortfolioActor(), UUID.randomUUID().toString)
        message.clinet ! PortfolioCreated(replyTo)
        Behaviors.same
    }
  }

  object PortfolioActor {
    sealed trait PortfolioCommand

    final case class Buy(stock: String, qty: Long) extends PortfolioCommand

    final case class Sell(stock: String, qty: Long) extends PortfolioCommand

    def apply(): Behavior[PortfolioCommand] = {
      portfolio(Portfolio(Map.empty))
    }

    private def portfolio(stocks: Portfolio): Behavior[PortfolioCommand] = {
      Behaviors.receiveMessage {
        case Buy(stock, qty) =>
          portfolio(stocks.buy(stock, qty))
        case Sell(stock, qty) =>
          portfolio(stocks.sell(stock, qty))
      }
    }
  }

  case class Stock(name: String, qty: Long) {
    def buy(number: Long): Stock = copy(name, qty + number)

    def sell(number: Long): Stock = {
      if (number < qty)
        copy(name, qty - number)
      else
        this
    }
  }

  case class Portfolio(stocks: Map[String, Stock]) {
    def buy(name: String, qty: Long): Portfolio = {
      val actuallyOwned: Stock = stocks.getOrElse(name, Stock(name, 0))
      copy(stocks + (name -> actuallyOwned.buy(qty)))
    }

    def sell(name: String, qty: Long): Portfolio = {
      val maybeOwned = stocks.get(name)
      // arg1 expression to evaluate if Empty arg1 function to apply if not empty
      maybeOwned.fold(this)(ownStock => copy(stocks + (name -> ownStock.sell(qty))))
    }
  }

}
