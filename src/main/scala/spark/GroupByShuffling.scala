package com.krushna
package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.Properties.isWin

object GroupByShuffling extends App {

  Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
  if (isWin) System.setProperty("hadoop.home.dir", System.getProperty("user.dir") + "\\winutils\\hadoop-3.3.1")
  @transient lazy val conf: SparkConf = new SparkConf().setMaster("local").setAppName("Example")
  @transient lazy val sc: SparkContext = new SparkContext(conf)

  val pairs = sc.parallelize(List((1, "one"), (2, "two"), (3, "Thress"), (4, "Four")))
  pairs.groupByKey()
  pairs.take(4).foreach(x=> println(x._1))
}
