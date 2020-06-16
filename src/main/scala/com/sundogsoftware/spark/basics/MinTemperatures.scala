package com.sundogsoftware.spark.basics

import org.apache.log4j._
import org.apache.spark.SparkContext
import scala.math.min

object MinTemperatures {

  def parseLine(line: String) = {
    val fields = line.split(",")
    val stationId = fields(0)
    val entryType = fields(2)
    val temperature = fields(3).toFloat * 0.1f * (9.0f / 5.0f) + 32.0f
    (stationId, entryType, temperature)
  }

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "MinTemperatures")
    val lines = sc.textFile("data/1800.csv")
    val parsedLines = lines.map(parseLine)
    val minTemps = parsedLines.filter(x => x._2.equals("TMIN"))
    // convert to (stationID, temperature)
    val stationTemps = minTemps.map(x => (x._1, x._3.toFloat))

    // Reduce by stationID retaining the minimum temperature found
    val minTempsByStation = stationTemps.reduceByKey((x, y) => min(x, y))

    // Collect, format, and print the results
    val results = minTempsByStation.collect()

    results.sorted.foreach(x => println(f"${x._1} station minimum temperature: ${x._2}%.2f F"))
    /*for (result <- results.sorted) {
      val station = result._1
      val temp = result._2
      //val formattedTemp = f"$temp%.2f F"
      //println(s"$station minimum temperature: $formattedTemp")
      println(f"$station minimum temperature: $temp%.2f F")
    }*/

  }
}
