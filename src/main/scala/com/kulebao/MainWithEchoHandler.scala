package com.kulebao

import akka.actor.ActorSystem
import com.kulebao.echo._
import com.kulebao.server.TcpServer
import com.typesafe.config.{Config, ConfigFactory}

object MainWithTcpHandler extends App {
  val conf: Config = ConfigFactory.load()
  val system = ActorSystem("server", conf)
  val service = system.actorOf(TcpServer.props(EchoHandlerProps), "ServerActor")
}

object MainWithDBHandler extends App {
  val system = ActorSystem("server")
  val service = system.actorOf(TcpServer.props(DbHandlerProps), "DBActor")
}
