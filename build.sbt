name := "apache-spark-with-scala"

version := "0.1"

organization := "com.sundogsoftware"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.3.2",
  "org.apache.spark" %% "spark-sql" % "2.3.2",
  "org.apache.spark" %% "spark-mllib" % "2.3.2"
)


// Setup follows example in "http://blog.miz.space/tutorial/2016/08/30/how-to-integrate-spark-intellij-idea-and-scala-install-setup-ubuntu-windows-mac/"

mainClass in (Compile, packageBin) := Some("com.sundogsoftware.spark.advanced.MovieSimilarities")


// On terminal
// sbt package
// sbt "runMain com.sundogsoftware.spark.advanced.MovieSimilarities 50"