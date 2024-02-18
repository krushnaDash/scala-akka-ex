package com.krushna
package kafkademo

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import java.util.Properties

object TestCocProducer {
  def main(args: Array[String]): Unit = {
    val kafkaproducer = new KafkaProducer[String, String](getProperties)


   /* val record = new ProducerRecord[String, String]("cocorders", "afbb5f56-6a80-33bf-af9e-42b9cfd9f8df", "{\"id\":\"afbb5f56-6a80-33bf-af9e-42b9cfd9f8df\",\"orderId\":\"afbb5f56-6a80-33bf-af9e-42b9cfd9f8df\",\"eventId\":\"5ef53142-782c-4fea-b009-c987e3e5e7aa\",\"customerOrderId\":\"6940224232848723280351\",\"businessDate\":\"2023-03-04\",\"orderDateTime\":\"2023-03-04T16:29:09-05:00\",\"orderType\":\"SalesOrder\",\"businessUnit\":{\"id\":\"6539\",\"baseDivisionCode\":\"18\",\"countryCode\":\"US\",\"financialReportingGroup\":\"US\"},\"categories\":[{\"categoryName\":\"RegisterTaxCode1\",\"identifier\":{\"terminalId\":\"4\"},\"amount\":123.55},{\"categoryName\":\"GiftCardRedemption\",\"amount\":12.41,\"orderIdentifiers\":[{\"paymentIds\":[\"c5bd488a-30b4-3c00-9d21-5cab12d652c5\"]}]},{\"categoryName\":\"Department\",\"identifier\":{\"salesDivision\":\"18\",\"departmentNumber\":\"84\"},\"amount\":3.3}],\"partitionKey\":\"2023-03-04-6539.US\"}")
    kafkaproducer.send(record)
    Thread.sleep(2000)

    val record2 = new ProducerRecord[String, String]("cocorders", "afbb5f56-6a80-33bf-af9e-42b9cfd9f8df-1", "{\"id\":\"afbb5f56-6a80-33bf-af9e-42b9cfd9f8df-1\",\"orderId\":\"afbb5f56-6a80-33bf-af9e-42b9cfd9f8df-1\",\"eventId\":\"5ef53142-782c-4fea-b009-c987e3e5e7aa-1\",\"customerOrderId\":\"6940224232848723280351\",\"businessDate\":\"2023-03-04\",\"orderDateTime\":\"2023-03-04T16:29:09-05:00\",\"orderType\":\"SalesOrder\",\"businessUnit\":{\"id\":\"6539\",\"baseDivisionCode\":\"18\",\"countryCode\":\"US\",\"financialReportingGroup\":\"US\"},\"categories\":[{\"categoryName\":\"RegisterTaxCode1\",\"identifier\":{\"terminalId\":\"4\"},\"amount\":123.55},{\"categoryName\":\"GiftCardRedemption\",\"amount\":12.41,\"orderIdentifiers\":[{\"paymentIds\":[\"c5bd488a-30b4-3c00-9d21-5cab12d652c5\"]}]},{\"categoryName\":\"Department\",\"identifier\":{\"salesDivision\":\"18\",\"departmentNumber\":\"84\"},\"amount\":3.3}],\"partitionKey\":\"2023-03-04-6539.US\"}")
    kafkaproducer.send(record2)
    Thread.sleep(2000)

    */

    // get the records from file
    for(w <- 7 to 21) {
      val source = scala.io.Source.fromFile("/Users/k0d03gd/Library/CloudStorage/OneDrive-WalmartInc/ADP/COC-messages/m"+w+".json")
      val lines = try source.mkString finally source.close()
      val record = new ProducerRecord[String, String]("cocorders", w.toString, lines)
      kafkaproducer.send(record)
      if(w%2==0)
      Thread.sleep(4000)
    }

  }
  def getProperties(): Properties = {
    val properties: Properties = new Properties
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties
  }
}
