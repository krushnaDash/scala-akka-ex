package com.krushna
package spark

import com.krushna.spark.SparkSql.spark
import org.apache.spark.sql.SparkSession

object DataSetEx extends App{
  //val spark: SparkSession = SparkSession.builder().master("local").getOrCreate()
  // this is for dataproc cluster
  val spark: SparkSession=SparkSession.builder().getOrCreate()

  val sc = spark.sparkContext
  val keyValue=List(("Akka","Krushna"),("Spark","Krushna"),("Akka","Rajesh"),("Spark","Gaurav"),("Akka","Cill Team"),("Akka","BUA team"))
  val rdd=sc.parallelize(keyValue).map(s=> (s._1,s._2))
  val res=rdd.reduceByKey(_+_)
  res.collect().foreach(s=> println(">>>>"+s))

  // to do the same in dataset
  import spark.implicits._
  val ds=rdd.toDS()
  // we can't use the reduce directly here, we need to apply the groupbyKey then reduce
  val resDs=ds.groupByKey(s=> s._1).mapValues(s=> s._2).reduceGroups((acc,str)=> acc+str)
  resDs.show()
}
