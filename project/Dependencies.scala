import Dependencies.akkaBase
import sbt._

object Versions {
  val akkaV = "2.6.17"
  val akkaHttpV = "10.2.7"
  val macwireV = "2.5.0"

  val scalaTestV = "3.1.2"
  val scalaMockV = "4.4.0"

  val circeV = "0.13.0"
  val akkaHttpCirceV = "1.33.0"

  val tapirV = "0.19.1"
  val pureConfigV = "0.17.1"
}

object Dependencies {

  import Versions._

  lazy val allDependencies: Seq[ModuleID] =
    akkaHttp ++ macwire ++ akkaBase ++ pureconfig ++ circe ++
      tapir ++ logs ++ test

  private lazy val macwire = Seq(
    "com.softwaremill.macwire" %% "macros" % macwireV % "provided",
    "com.softwaremill.macwire" %% "util" % macwireV,
    "com.softwaremill.macwire" %% "proxy" % macwireV
  )
  private lazy val akkaBase = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV
  )

  private lazy val tapir = Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirV,
    "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % tapirV,
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirV,
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirV,
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirV,
    "com.softwaremill.sttp.tapir" %% "tapir-derevo" % tapirV,
    "com.softwaremill.sttp.tapir" %% "tapir-newtype" % tapirV
  )

  private lazy val akkaHttp = Seq(
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % "test"
  )

  private lazy val circe = Seq(
    "io.circe" %% "circe-core" % circeV,
    "io.circe" %% "circe-generic" % circeV,
    "io.circe" %% "circe-parser" % circeV,
    "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceV
  )

  private lazy val test = Seq(
    "org.scalatest" %% "scalatest" % scalaTestV % "test",
    "org.scalamock" %% "scalamock" % scalaMockV % "test"
  )

  private lazy val logs = Seq(
    "org.slf4j" % "slf4j-simple" % "1.7.32",
    "log4j" % "log4j" % "1.2.17"
  )

  private lazy val pureconfig = Seq(
    "com.github.pureconfig" %% "pureconfig" % pureConfigV
  )
}