package com.kulebao

import akka.actor.ActorSystem
import com.kulebao.echo._
import com.kulebao.server.TcpServer

//
//object TestServer {
//  def main(args: Array[String]) {
//    println("hello")
//    val create: ActorSystem = ActorSystem.create("mySystem")
//    val actor: ActorRef = create.actorOf(Props.create(classOf[MyActor]), "myActor")
//    actor.tell("hello world", ActorRef.noSender)
//    create.shutdown()
//  }
//}

object MainWithEchoHandler extends App {
  val system = ActorSystem("server")
  val service = system.actorOf(TcpServer.props(EchoHandlerProps), "ServerActor")
}
