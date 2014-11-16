import sbt._

object Version {
  val akka      = "2.3.6"
  val akkaHttp  = "0.9"
  val logback   = "1.1.2"
  val scala     = "2.11.2"
  val scalaTest = "2.2.2"
  val play      = "2.4.0-M1"
  val joda      = "2.5"
}

object Library {
  val akkaActor       = "com.typesafe.akka" %% "akka-actor"                    % Version.akka
  val akkaPersistence = "com.typesafe.akka" %% "akka-persistence-experimental" % Version.akka
  val akkaStream      = "com.typesafe.akka" %% "akka-stream-experimental"      % Version.akkaHttp
  val akkaHttp        = "com.typesafe.akka" %% "akka-http-core-experimental"   % Version.akkaHttp
  val akkaSlf4j       = "com.typesafe.akka" %% "akka-slf4j"                    % Version.akka
  val akkaTestkit     = "com.typesafe.akka" %% "akka-testkit"                  % Version.akka
  val logbackClassic  = "ch.qos.logback"    %  "logback-classic"               % Version.logback
  val scalaTest       = "org.scalatest"     %% "scalatest"                     % Version.scalaTest
  val playJson        = "com.typesafe.play" %% "play-json"                     % Version.play
  val joda            = "joda-time"         %  "joda-time"                     % Version.joda
}

object Dependencies {

  import Library._

  val buyTicket = List(
    akkaActor,
    akkaPersistence,
    akkaStream,
    akkaHttp,
    akkaSlf4j,
    logbackClassic,
    playJson,
    joda,
    akkaTestkit % "test",
    scalaTest   % "test"
  )
}
