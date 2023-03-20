package com.krushna
package spark

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{StringType, StructField, StructType}

import scala.util.Properties.isWin

object SparkSql extends App{
  if (isWin) System.setProperty("hadoop.home.dir", System.getProperty("user.dir") + "\\winutils\\hadoop-3.3.1")
  val spark: SparkSession = SparkSession.builder().master("local").getOrCreate()
  val sc = spark.sparkContext

  // creating data frame
  val rdd = sc.parallelize(Seq(
      ("John", "Manager", 38),
      ("Mary", "Director", 45),
      ("Sally", "Engineer", 30)
    ))

  val dfWitDefaultSchema = spark.createDataFrame(rdd)

  import spark.implicits._
  val df=rdd.toDF() // to use the toDF method we need to import the import spark.implicits._, this can convert an RDD to Dataframe

  // register the dataframe as tempeopry SQL view
  df.createOrReplaceTempView("people")

  val mangerAndabove=spark.sql(""" select * from people where _2 = "Manager" or _2 = "Director" """)

  mangerAndabove.take(10).foreach(r=> println(">>>"+r))
  df.show()

  // use the dataframe API instead a SQL
  val data=df.select("_1","_2").
    where("_2 == 'Manager' or _2 == 'Director' ")
    .orderBy("_1")


  // creating a data frame by passing a schema
  val peopleRdd=sc.parallelize(List(Persion(100,"krushna")))

  val schemaString="id name"
  val fileds=schemaString.split(" ").map(f => StructField(f,StringType))
  val schmea=StructType(fileds)

  //spark.createDataFrame(peopleRdd,schmea)

  //val dataframe=spark.read.json("")
  //val dataframe2=spark.read.jdbc("")



}

case class Persion(id:Int, name:String)
