package com.kulebao.echo

import akka.actor._
import com.kulebao.handler._


object DispatcherProps extends HandlerProps {
  def props(connection: ActorRef) = Props(classOf[Dispatcher], connection)
}

class Dispatcher(connection: ActorRef) extends Handler(connection) {

  def received(data: String) = {
    log.debug(s"received: $data")
    val dbWriter: ActorRef = context.actorOf(Props[DbWriter])
    val linkAnswer: ActorRef = context.actorOf(AnswerLinkProps.props(connection))
    data.split("#\\*").foreach {
      (cmd) =>
        dbWriter ! cmd
        linkAnswer ! cmd
    }

  }


}
