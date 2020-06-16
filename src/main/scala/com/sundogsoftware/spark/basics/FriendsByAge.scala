package com.sundogsoftware.spark.basics

import org.apache.log4j._
import org.apache.spark._

/** Compute the average number of friends by age in a social network. */
object FriendsByAge {

  /** A function that splits a line of input into (age, numFriends) tuples. */
  def parseLine(line: String) = {
    val fields = line.split(",")
    val age = fields(2).toInt
    val numFriends = fields(3).toInt
    (age, numFriends)
  }

  /** Our main function where the action happens */
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "FriendsByAge")
    val lines = sc.textFile("data/fakefriends.csv")
    val rdd = lines.map(parseLine)

    val totalsByAge = rdd.mapValues(x => (x, 1)).reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))
    val averageByAge = totalsByAge.mapValues(x => x._1 / x._2)
    val result = averageByAge.collect()

    result.sorted.foreach(println)

  }

}
