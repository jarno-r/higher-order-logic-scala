import Dependencies._

ThisBuild / scalaVersion     := "3.3.3"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "logic"

lazy val root = (project in file("."))
  .settings(
    name := "higher-order-logic-scala",
    libraryDependencies += munit % Test,
    scalacOptions += "-explain"
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
