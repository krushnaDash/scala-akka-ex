package com.krushna
package spark

import com.krushna.spark.SparkSql.spark
import org.apache.spark.sql.SparkSession
//import org.apache.log4j.{Level, Logger}

object WhySparkSql extends App{
  val spark: SparkSession = SparkSession.builder().master("local").getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")
  val sc = spark.sparkContext
  val demographic=sc.parallelize(List(Demographic(1001,25,true,"Swiss","Male",true,true)))
  val finances= sc.parallelize(List(Finances(1001,true,true,true,1500)))
  // swiss Student
  // have debt and financial dependent

  val swissStudent=demographic.map(p=> (p.id,p)).filter(p=> p._2.country.equals("Swiss"))

  val hasDebtAndFinancialDependent=finances.map(p=> (p.id,p)).filter(f=> f._2.hasDebt&&f._2.hasFinancialDependent)
  val targetStudent=swissStudent.join(hasDebtAndFinancialDependent).count()

  println(">>>>>>"+targetStudent)

  // using dataframe where optimisation happen by spark

  import spark.implicits._
  val demographicDF=demographic.toDF()
  val financesDF=finances.toDF()

  val result=demographicDF.join(financesDF, demographicDF("id")=== financesDF("id"), "inner" ).
    filter($"hasDebt" && $"hasFinancialDependent").
    filter($"country" === "Swiss").
    count()

  println(">>>>>>***"+ result)

  // using dataset with type Safty
  // type DataFrame = Dataset[Row]
  // to create type dataset from a JSON file
 // val myds=spark.read.json("").as[Finances]

  val demographicDs=demographic.toDS()
  val financesDS=finances.toDS()



}

case class Demographic(id: Int, age: Int,
                       coadingBootcamp: Boolean,
                       country: String,
                       gender: String,
                       isEthinicMiniority: Boolean,
                       servedMiltery: Boolean)

case class Finances(id:Int,
                    hasDebt:Boolean,
                    hasFinancialDependent:Boolean,
                    hasStudentLoan:Boolean,
                    income:Int)

