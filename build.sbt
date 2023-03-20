ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "scala-akka-ex",
    idePackagePrefix := Some("com.krushna")
  )
mainClass := Some("com.krushna.other.HelloWorld")
lazy val akkaVersion = "2.6.19"

libraryDependencies ++= Seq(
  excludes("org.apache.spark" %% "spark-sql" % "3.2.2"),
  excludes("org.apache.spark" %% "spark-core" % "3.2.0"),
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-typed" % akkaVersion,
  "com.ning" % "async-http-client" % "1.9.40",
  "org.jsoup" % "jsoup" % "1.14.3",
  "ch.qos.logback" % "logback-classic" % "1.2.11",
  "org.apache.kafka" % "kafka-clients" % "2.4.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka"%%"akka-persistence" %akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.2.12" % Test,
  "org.scalameta" %% "munit" % "0.7.29" % Test
)

//netty-all replaces all these excludes
def excludes(m: ModuleID): ModuleID =
  m.exclude("io.netty", "netty-common").
    exclude("io.netty", "netty-handler").
    exclude("io.netty", "netty-transport").
    exclude("io.netty", "netty-buffer").
    exclude("io.netty", "netty-codec").
    exclude("io.netty", "netty-resolver").
    exclude("io.netty", "netty-transport-native-epoll").
    exclude("io.netty", "netty-transport-native-unix-common").
    exclude("javax.xml.bind", "jaxb-api").
    exclude("jakarta.xml.bind", "jaxb-api").
    exclude("javax.activation", "activation").
    exclude("jakarta.annotation", "jakarta.annotation-api").
    exclude("javax.annotation", "javax.annotation-api")