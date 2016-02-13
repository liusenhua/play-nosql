name := """play-nosql"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus" %% "play" % "1.1.0" % "test",
  "com.wordnik" %% "swagger-play2" % "1.3.10",
  "org.reactivecouchbase" %% "reactivecouchbase-play" % "0.3"
)

//libraryDependencies += "com.couchbase.client" % "java-client" % "2.2.0"


resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"


resolvers += "ReactiveCouchbase repository" at "https://raw.github.com/ReactiveCouchbase/repository/master/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
//routesGenerator := InjectedRoutesGenerator
