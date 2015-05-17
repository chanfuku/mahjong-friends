name := """mahjong-friends"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

lazy val skinnyVersion = "1.3.+"

libraryDependencies ++= Seq(
  "org.mariadb.jdbc" % "mariadb-java-client" % "1.1.8",
  "com.h2database" % "h2" % "1.4.+" % "test",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.skinny-framework" %% "skinny-orm" % "1.3.14",
  "org.scalikejdbc" %% "scalikejdbc-test" % "2.2.+" % "test",
  "org.fluentd" % "fluent-logger" % "0.3.1"
)
