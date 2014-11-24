package com.kulebao.echo

import java.util.Date

import akka.actor._
import akka.io.Tcp.Write
import akka.util.ByteString
import com.kulebao.handler._
import org.joda.time.DateTime
import com.kulebao.db.DB


object EchoHandlerProps extends HandlerProps {
  def props(connection: ActorRef) = Props(classOf[EchoHandler], connection)
}

class EchoHandler(connection: ActorRef) extends Handler(connection) with DB {

  def received(data: String) = {
    connection ! Write(reply(data))
    val actor: ActorRef = context.actorOf(Props[DbWriter])
    actor ! data
  }

  def reply(input: String): ByteString = {
    log.debug(s"receive: $input")
    val prefix = input.split(",").take(2).mkString(",")

    val time: DateTime = DateTime.now()
    val formattedTime = time.toString("HHmmss")

    val output: String = s"$prefix,D1,$formattedTime,60,1#"
    log.debug(s"reply: $output")
    ByteString(output)
  }
}
