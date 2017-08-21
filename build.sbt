import sbtassembly.MergeStrategy

enablePlugins(JavaAppPackaging)

organization := "mwojcik"
name := "rss-customizer"
version := "1.0-SNAPSHOT"
scalaVersion := "2.11.11"

val AkkaHttpVersion = "10.0.9"
val ScalaTestVersion = "3.0.1"
val SlickVersion = "3.1.1"
val CirceVersion = "0.8.0"
val AkkaVersion = "2.4.20"


resolvers ++= Seq(
  Resolver.bintrayRepo("hseeberger", "maven"),
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/"
)

libraryDependencies ++= {

val akkaHttp = Seq(
  "com.typesafe.akka" %% "akka-http-core" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-xml" % AkkaHttpVersion
)

val logging = Seq(
  "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "ch.qos.logback" % "logback-classic" % "1.1.7"
)

val config = Seq(
  "com.github.melrief" %% "pureconfig" % "0.6.0" exclude("com.typesafe", "config")
)

val test = Seq(
  "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion,
  "org.scalactic" %% "scalactic" % ScalaTestVersion,
  "org.scalatest" %% "scalatest" % ScalaTestVersion,
  "org.mockito" % "mockito-core" % "1.8.5",
  "com.typesafe.akka" %% "akka-testkit" % AkkaVersion
).map(_ % "test")


val parsers = Seq(
  "de.heikoseeberger" %% "akka-http-circe" % "1.17.0",
  "net.ruippeixotog" %% "scala-scraper" % "2.0.0",
  "io.circe" %% "circe-generic" % CirceVersion exclude("aopalliance", "aopalliance")
)


  akkaHttp ++ logging ++ config ++ test ++ parsers
}
