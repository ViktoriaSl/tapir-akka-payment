name := "bitclear-project"

version := "0.1"

scalaVersion := "2.13.7"

resolvers ++= Seq(
  Resolver.typesafeRepo("releases")
)

libraryDependencies ++= Dependencies.allDependencies

//libraryDependencies ++= {
//  val akkaVersion = "latest.release"
//
//  Seq(
//    "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
//    "com.typesafe.akka" %% "akka-http-spray-json" % akkaVersion,
//    "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion,
//    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
//  )
//}
