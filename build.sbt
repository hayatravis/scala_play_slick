name := """ad"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
//  jdbc,
//  cache,
//  ws,
//  specs2 % Test,
//	"mysql" % "mysql-connector-java" % "5.1.24",
	"com.h2database" % "h2" % "1.4.177",
	"com.typesafe.play" %% "play-slick" % "1.1.0",
	"com.typesafe.play" %% "play-slick-evolutions" % "1.1.0",
	"org.slf4j" % "slf4j-nop" % "1.6.4"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
