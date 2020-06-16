package com.sundogsoftware.spark.basics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

object WordCount extends App {
  Logger.getLogger("org").setLevel(Level.ERROR)

  val sc = new SparkContext("local[*]", "WordCount")
  val input = sc.textFile("data/book.txt")
  val words = input.flatMap(x => x.split("\\W+"))

  val lowerCaseWords = words.map(x => x.toLowerCase)
  //val wordCount = words.countByValue()
  val wordCounts = lowerCaseWords.map(x => (x, 1)).reduceByKey((x, y) => x + y)
  val wordCountsSorted = wordCounts.map(x => (x._2, x._1)).sortByKey()

  for (result <- wordCountsSorted) {
    println(s"${result._2}: ${result._1}")
  }
}
