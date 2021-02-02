
name := "proxy-server"

version := "0.1"

scalaVersion := "2.13.4"

lazy val akkaHttpVersion = "10.1.12"
lazy val akkaVersion    = "2.5.31"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream"          % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion         % "test",
  "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion     % "test",
  "org.scalatest"     %% "scalatest"            % "3.2.2"             % "test",
  "org.scalamock"     %% "scalamock"            % "4.4.0"             % "test"
)

enablePlugins(AkkaGrpcPlugin)