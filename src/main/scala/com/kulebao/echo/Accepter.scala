package com.kulebao.echo

import akka.io.Tcp.Write
import com.kulebao.handler._
import akka.actor._
import akka.util.ByteString
import org.joda.time.DateTime


object EchoHandlerProps extends HandlerProps {
  def props(connection: ActorRef) = Props(classOf[EchoHandler], connection)
}

class EchoHandler(connection: ActorRef) extends Handler(connection) {

  /**
   * Echoes incoming message.
   */
  def received(data: String) = connection ! Write(reply(data))

  def reply(input: String): ByteString = {
    val prefix = input.split(",").take(2).mkString(",")

    val time: DateTime = DateTime.now()
    val formattedTime = time.toString("HHmmss")

    ByteString(s"$prefix,D1,$formattedTime,5,1#")
  }
}
