name := "alert_term_extractor"
version := "0.1"
scalaVersion := "2.13.12"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.19" % Test,
  "org.mockito" %% "mockito-scala" % "1.17.37" % Test, // Mockito for mocking // TODO: remove if not used
  "com.lihaoyi" %% "requests" % "0.9.0",
  "com.lihaoyi" %% "upickle" % "4.1.0",
  "org.apache.logging.log4j" % "log4j-api" % "2.24.3",
  "org.apache.logging.log4j" % "log4j-core" % "2.24.3",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.24.3"
)