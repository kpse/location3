package com.kulebao.echo

import akka.actor.{ActorLogging, ActorRef, Props, UntypedActor}
import akka.io.Tcp.Write
import akka.util.ByteString
import com.kulebao.CmdPattern
import com.kulebao.handler.HandlerProps
import org.joda.time.DateTime

object AnswerLinkProps extends HandlerProps {
  def props(connection: ActorRef) = Props(classOf[AnswerLink], connection)
}

class AnswerLink(connection: ActorRef) extends UntypedActor with ActorLogging {

  @throws(classOf[Exception])
  override def onReceive(message: Any): Unit = {
    message match {
      case data: String =>

        println(s"AnswerLink received: $data")
        log.debug(s"received: $data")
        data match {
          case CmdPattern.cmdLink(id, time, gsm, gps, bat, step, turnover, ex1, ex2, date, status) =>
            log.debug("link cmd, reply D1 cmd")
            connection ! Write(reply(data))
          case _ =>
            log.debug("not a link cmd, don't reply")
        }
      case _ =>
        log.debug(s"not link cmd: $message")
    }
  }


  def reply(input: String): ByteString = {
    println(input)
    val prefix = input.split(",").take(2).mkString(",")

    val formattedTime = DateTime.now().toString("HHmmss")

    val output: String = s"$prefix,D1,$formattedTime,60,1#"
    log.debug(s"reply: $output")
    ByteString(output)
  }
}
