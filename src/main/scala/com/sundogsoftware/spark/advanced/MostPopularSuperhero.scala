package com.sundogsoftware.spark.advanced

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

object MostPopularSuperhero {

  // Function to extract the hero ID and number of connections from each line
  def countCoOccurences(line: String): (Int, Int) = {
    val elements = line.split("\\s+")
    (elements(0).toInt, elements.length - 1)
  }

  // Function to extract hero ID -> hero name tuples (or None in case of failure)
  def parseNames(line: String): Option[(Int, String)] = {
    val fields = line.split('\"')
    if (fields.length > 1) {
      Some(fields(0).trim().toInt, fields(1))
    } else {
      None // flatMap will just discard None results, and extract data fro Some results.
    }
  }

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "MostPopularSuperhero")

    val names = sc.textFile("data/Marvel-names.txt")
    val namesRdd = names.flatMap(parseNames)

    val lines = sc.textFile("data/Marvel-graph.txt")
    val pairings = lines.map(countCoOccurences)

    val totalFriendsByCharacter = pairings.reduceByKey((x, y) => x + y)
    val mostPopular = totalFriendsByCharacter.max()(Ordering[Int].on(x => x._2))
    val mostPopularName = namesRdd.lookup(mostPopular._1)(0)
    //val mostPopular = totalFriendsByCharacter.map(x => (x._2, x._1)).max
    //val mostPopularName = namesRdd.lookup(mostPopular._2)(0)

    println(s"$mostPopularName is the most popular superhero with ${mostPopular._2} co-apperances.")

  }

}
