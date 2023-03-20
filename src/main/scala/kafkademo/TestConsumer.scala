package com.krushna
package kafkademo

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, KafkaConsumer}

import java.time.Duration
import java.util.{Collections, Properties}


object TestConsumer extends App {

  val consumer= new KafkaConsumer[String,String](getProperties())
  consumer.subscribe(Collections.singletonList("wm-cth-salesstreams"))

  while(true){
    val data = consumer.poll(Duration.ofSeconds(3))
    data.forEach(println(_))
  }

  def getProperties(): Properties = {
    val properties: Properties = new Properties
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "scala-c1-1234")
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    properties
  }
}
