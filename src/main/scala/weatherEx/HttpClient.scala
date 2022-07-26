package com.krushna
package weatherEx
import scala.concurrent.{Future}
import scala.concurrent.ExecutionContext.Implicits.global
class HttpClient {
  def get(url: String): Future[Weather] =
    if (url.contains("29/04/2022"))
      Future(Weather("Sunny"))
    else if (url.contains("30/04/2022"))
      Future(Weather("Windy"))
    else {
      Future {
        throw new RuntimeException
      }
    }
}