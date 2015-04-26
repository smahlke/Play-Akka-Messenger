name := """Play-Messenger-App"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"


libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.27",
  javaJdbc,
  javaJpa.exclude("org.hibernate.javax.persistence", "hibernate-jpa-2.0-api"), 
  "org.hibernate" % "hibernate-entitymanager" % "4.3.8.Final",
  cache,
  javaWs
)

fork in run := true