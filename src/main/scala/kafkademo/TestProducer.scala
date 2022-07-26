package com.krushna
package kafkademo

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import java.util.Properties


object TestProducer {
  def main(args: Array[String]): Unit = {
    val kafkaproducer = new KafkaProducer[String, String](getProperties)

    for (i <- 1 to 10) {
      val record = new ProducerRecord[String, String]("kfaka-demo", "3000" + i, "Random Value" + i)
      kafkaproducer.send(record)
      Thread.sleep(2000)
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
