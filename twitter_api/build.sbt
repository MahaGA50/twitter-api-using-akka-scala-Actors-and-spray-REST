name := "twitter_scala_api"

version := "0.1"

scalaVersion := "2.11.7"

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
                  "Spray Repository"    at "http://repo.spray.io")


libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor"      % "2.3.9",
    "com.typesafe.akka" %% "akka-slf4j"      % "2.3.9",
	"org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23",
	"io.spray"          %% "spray-can"       % "1.3.2",
    "io.spray"          %% "spray-routing"   % "1.3.2",
    "io.spray"          %% "spray-json"      % "1.3.1",
    "io.spray"          %% "spray-testkit"   % "1.3.2" % "test",
    "com.typesafe.akka" %% "akka-testkit"    % "2.3.9"  % "test",
    "org.specs2"        %% "specs2"          % "2.3.13"     % "test")


