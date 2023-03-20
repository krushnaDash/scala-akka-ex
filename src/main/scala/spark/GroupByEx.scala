package com.krushna
package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{RangePartitioner, SparkConf, SparkContext}

import scala.util.Properties.isWin

case class CFFPurchase(cusid: Int,destination: String,amount: Double)

object GroupByEx extends App {
  Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
  if (isWin) System.setProperty("hadoop.home.dir", System.getProperty("user.dir") + "\\winutils\\hadoop-3.3.1")
  @transient lazy val conf: SparkConf = new SparkConf().setMaster("local").setAppName("Example")
  @transient lazy val sc: SparkContext = new SparkContext(conf)

  val customerData = List(CFFPurchase(100, "Bheden", 100), CFFPurchase(100, "Zurich", 1000), CFFPurchase(100, "Stabio", 50),
    CFFPurchase(101, "Bargarh", 20), CFFPurchase(101, "SonePur", 50), CFFPurchase(101, "Kutpali", 70),
    CFFPurchase(100, "Bheden", 100),
    CFFPurchase(102, "Bheden", 100), CFFPurchase(102, "Bheden", 100))

  val rddCustomer = sc.parallelize(customerData)

  // find how many trip and how much money spend by each customer

  // Solutions 1

  val result=rddCustomer.map(p=> (p.cusid,p.amount)).groupByKey().map(s=> (s._1, (s._2.sum, s._2.size ) )).collect()
  result.foreach(v=> println(">>>>"+v._1+"  "+" amount "+ v._2._1+ " trip " + v._2._2))

  // Solutions 2

  val result2=rddCustomer.map(p=> (p.cusid, (1,p.amount) )).reduceByKey((v1,v2) => (v1._1+v2._1, v1._2+v2._2) )
  result2.foreach(v=> println(">>>>"+v._1+"  "+" trip "+ v._2._1+ " amount " + v._2._2))

  // partioning the data using a range partion

  val paris=rddCustomer.map(p=> (p.cusid,p.amount))
  val tunedPartionor=new RangePartitioner(8,paris)
  val partionedData=paris.partitionBy(tunedPartionor).persist()

}
