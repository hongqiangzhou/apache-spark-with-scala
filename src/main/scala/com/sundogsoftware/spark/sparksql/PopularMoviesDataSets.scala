package com.sundogsoftware.spark.sparksql

import java.nio.charset.CodingErrorAction

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

import scala.io.{Codec, Source}

object PopularMoviesDataSets {

  /** Load up a Map of movie IDs to movie names. */
  def loadMovieNames(): Map[Int, String] = {
    // Handle character encoding issues:
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

    // Create a Map of Ints to Strings, and populate it from u.item.
    var movieNames: Map[Int, String] = Map()
    val lines = Source.fromFile("data/ml-100k/u.item").getLines()
    for (line <- lines) {
      val fields = line.split('|')
      if (fields.length > 1) {
        movieNames += (fields(0).toInt -> fields(1))
      }
    }

    movieNames
  }

  // Case class so we can get a column name for our movie ID
  final case class Movie(movieID: Int)

  /** Our main function where the action happens */
  def main(args: Array[String]): Unit = {
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Use new SparkSession interface in Spark 2.0
    val spark = SparkSession.builder.appName("PopularMovies").master("local[*]").getOrCreate()

    // Read in each rating line and extract the movie ID; construct an RDD of Movie objects.
    val lines = spark.sparkContext.textFile("data/ml-100k/u.data").map(x => Movie(x.split("\t")(1).toInt))

    // Convert to a DataSet
    import spark.implicits._
    val moviesDS = lines.toDS()

    // Some SQL-style magic to sort all movies by popularity in one line!
    val topMovieIDs = moviesDS.groupBy("movieID").count().orderBy(desc("count")).cache()
    topMovieIDs.show()

    // Grab the top 10
    val top10 = topMovieIDs.take(10)

    // Load up the movie ID -> name map
    val names = loadMovieNames()

    // Print the results
    for (result <- top10){
      println(names(result(0).asInstanceOf[Int]) + ": " + result(1))
    }

    spark.stop()
  }

}
