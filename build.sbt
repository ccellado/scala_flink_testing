import Dependencies._

ThisBuild / scalaVersion     := "2.12.11"
ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "org.tt"
ThisBuild / organizationName := "tt"

addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")

lazy val flinkVersion = "1.10.0"

lazy val root = (project in file("."))
  .settings(
    name := "flink-mock-datastream",
    libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "3.2.2"
        , "org.scalacheck" %% "scalacheck" % "1.15.2"
        , "org.scalatestplus" %% "scalacheck-1-14" % "3.2.2.0"
        , "org.slf4j" % "slf4j-log4j12" % "1.7.30" % Provided
        , "org.apache.flink" %% "flink-test-utils" % flinkVersion
        , "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % Provided
        , "org.apache.flink" %% "flink-scala" % flinkVersion % Provided
    )
  )

Compile / run := Defaults.runTask(Compile / fullClasspath,
  Compile / run / mainClass,
  Compile / run / runner
).evaluated

// stays inside the sbt console when we press "ctrl-c" while a Flink programme executes with "run" or "runMain"
Compile / run / fork := true
Global / cancelable := true