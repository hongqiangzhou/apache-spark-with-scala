package com.sundogsoftware.spark.basics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

object PurchaseByCustomer {

  def extractCustomerPricePairs(line: String): (Int, Float) = {
    val fields = line.split(",")
    (fields(0).toInt, fields(2).toFloat)
  }

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "TotalSpentByCustomer")
    val lines = sc.textFile("data/customer-orders.csv")
    val mappedInput = lines.map(extractCustomerPricePairs)
    val totalByCustomer = mappedInput.reduceByKey((x, y) => x + y)
    val result = totalByCustomer.collect().sortBy(x => x._2)

    result.foreach(println)
  }
}
