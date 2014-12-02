package com.kulebao.echo

import akka.actor._
import com.kulebao.handler._


object EchoHandlerProps extends HandlerProps {
  def props(connection: ActorRef) = Props(classOf[EchoHandler], connection)
}

class EchoHandler(connection: ActorRef) extends Handler(connection) {

  def received(data: String) = {
    log.debug(s"received: $data")
    val actor: ActorRef = context.actorOf(Props[DbWriter])
    val linkAnswer: ActorRef = context.actorOf(AnswerLinkProps.props(connection))
    data.split("#\\*").foreach {
      (cmd) =>
        actor ! cmd
        linkAnswer ! cmd
    }

  }


}
