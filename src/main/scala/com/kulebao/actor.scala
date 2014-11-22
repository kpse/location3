package com.kulebao

import akka.actor._

class MyActor extends UntypedActor {
  @throws[Exception](classOf[Exception])
  override def onReceive(message: Any): Unit = println(message)
}