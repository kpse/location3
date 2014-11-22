package com.kulebao.server

import java.net.InetSocketAddress

import akka.io.{IO, Tcp}
import akka.actor.{Actor, Props}

import com.kuelbao.server.Server
import com.kulebao.handler._

object TcpServer {
  def props(handlerProps: HandlerProps): Props =
    Props(classOf[TcpServer], handlerProps)
}

class TcpServer(handlerProps: HandlerProps) extends Server {

  import context.system

  IO(Tcp) ! Tcp.Bind(self, new InetSocketAddress("", 8888))

  override def receive = {
    case Tcp.CommandFailed(_: Tcp.Bind) => context stop self

    case Tcp.Connected(remote, local) =>
      val handler = context.actorOf(handlerProps.props(sender))
      sender ! Tcp.Register(handler)
  }

}