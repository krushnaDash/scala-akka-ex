package com.krushna
package weatherEx

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

class WeatherForecastService(val http: HttpClient) {
  var lastWeatherValue=Weather("Sunny")
  def forecast(date: String): Future[Weather] = {
    // the transform method creates a new Future by applying the specified function to the result of this Future.
    http.get(s"http://weather.now/rome?when=$date").transform{
      case Success(value) => {
        val retrieved = value
        lastWeatherValue = retrieved
        Try(retrieved)
      }
      case Failure(exception) => {
        println(s"Something went wrong, ${exception.getMessage}")
        Try(lastWeatherValue)
      }
    }
  }
}