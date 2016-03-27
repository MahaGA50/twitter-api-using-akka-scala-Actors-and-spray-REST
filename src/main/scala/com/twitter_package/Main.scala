package com.twitter_package

import akka.actor._
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import spray.can.Http

import scala.concurrent.duration._

object Main extends App {
  val config = ConfigFactory.load()
  // val host = config.getString("http.host")
  // val port = config.getInt("http.port")

  implicit val system = ActorSystem("management-service")

  val api = system.actorOf(Props(new AkkaJsonParsing()), "httpInterface")

  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(10 seconds)

  IO(Http).ask(Http.Bind(listener = api,"localhost",8090))
    .mapTo[Http.Event]
    .map {
      case Http.Bound(address) =>
        println(s"REST interface bound to $address")
      case Http.CommandFailed(cmd) =>
        println("REST interface could not bind to " +
          s"localhost:8080, ${cmd.failureMessage}")
        system.shutdown()
    }
}
