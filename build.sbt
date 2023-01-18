name := "history-service"

version := "0.1"

scalaVersion := "2.13.8"

val AkkaVersion = "2.7.0"
val AkkaHttpVersion = "10.4.0"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion
)

libraryDependencies += "com.google.cloud" % "google-cloud-storage" % "2.14.0"
libraryDependencies += "com.google.cloud" % "google-cloud-datastore" % "2.12.3"

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case PathList("reference.conf") => MergeStrategy.concat
  case _                        => MergeStrategy.first
}
