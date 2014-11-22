package com.kulebao.echo

import akka.io.Tcp.Write
import com.kulebao.handler._
import akka.actor._
import akka.util.ByteString


object EchoHandlerProps extends HandlerProps {
  def props(connection: ActorRef) = Props(classOf[EchoHandler], connection)
}

class EchoHandler(connection: ActorRef) extends Handler(connection) {

  /**
   * Echoes incoming message.
   */
  def received(data: String) = connection ! Write(ByteString(data + "\n"))
}
