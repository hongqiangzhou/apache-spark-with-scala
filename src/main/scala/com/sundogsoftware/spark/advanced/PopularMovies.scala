package com.sundogsoftware.spark.advanced

import java.nio.charset.CodingErrorAction

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

import scala.io.{Codec, Source}

object PopularMovies {

  def loadMovieNames(): Map[Int, String] =  {
    // Handle character encoding issues:
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

    // Create a Map of Ints to Strings, and populate it from u.item
    var movieNames:Map[Int, String] = Map()

    val lines = Source.fromFile("data/ml-100k/u.item").getLines()
    for (line <- lines) {
      val fields = line.split('|')
      if (fields.length > 1) {
        movieNames += (fields(0).toInt -> fields(1))
      }
    }

    movieNames
  }

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "PopularMovies")

    // Create a broadcast variable of our Id -> movie name map
    val nameDict = sc.broadcast(loadMovieNames)

    val lines = sc.textFile("data/ml-100k/u.data")

    // Map to (movieId, 1)
    val movies = lines.map(x => (x.split("\t")(1).toInt, 1))

    val movieCounts = movies.reduceByKey((x, y) => x + y)
    val sortedMovies = movieCounts.sortBy(x => x._2)
    // Fold in the movie names from the broadcast variable
    val sortedMoviesWithNames = sortedMovies.map(x => (nameDict.value(x._1), x._2))

    sortedMoviesWithNames.collect().foreach(println)
  }

}
