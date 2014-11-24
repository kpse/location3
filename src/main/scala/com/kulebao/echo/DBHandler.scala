package com.kulebao.echo

import com.kulebao.db.DB
import com.kulebao.handler.Handler
import akka.util.ByteString
import com.github.mauricio.async.db.RowData
import java.util.Date
import akka.io.Tcp.Write
import akka.actor._
import com.kulebao.handler.HandlerProps
import scala.concurrent.ExecutionContext.Implicits.global

object DbHandlerProps extends HandlerProps {
  def props(connection: ActorRef) = Props(classOf[DbHandler], connection)
}

class DbHandler(connection: ActorRef) extends Handler(connection) with DB {

  /**
   * Writes incoming message to database and returns all data in db to user
   * @return
   */
  def received(data: String) {
    log.debug(data)
    println(data)
    execute("INSERT INTO demo (data) VALUES (?)", data + "--" + new Date).foreach(_ => printAll())
  }

  /**
   * Prints all data in db to user
   */
  def printAll() {
    respond("values in db are:")
    for {
      queryResult <- fetch("SELECT * FROM demo")
      resultSet <- queryResult
      rowData <- resultSet
      result = getData(rowData)
    } respond(result)
  }

  /**
   * Convert given data and send it to user
   * @param response: String
   */
  def respond(response: String) {
    log.debug(response)
    println(response)
    connection ! Write(ByteString(response + "\n"))
  }

  def getData(rowData: RowData) = {
    rowData("data").asInstanceOf[String]
  }
}

class DbWriter extends UntypedActor with DB with ActorLogging {

  @throws(classOf[Exception])
  override def onReceive(message: Any): Unit = {
    message match {
      case data: String =>
        data.split(",") match {
          case fields: Array[String] if fields.length == 13 =>
            val all = fields.tail.mkString("','").replaceFirst("#$", "")
            val sql: String = s"INSERT INTO records (device_id, cmd, time, valid, latitude, latitude_direction, longitude, longitude_direction, speed, direction, date, tracker_status) values ('$all');"
            execute(sql)
            log.debug(sql)
          case _ =>
            log.debug(s"record to demo: $data")
            log.debug(s"record to demo: $data")
            execute("INSERT INTO demo (data) VALUES (?)", data + "--" + new Date)
        }
      case _ =>
        log.debug("not supported!")
        println("not supported!")
    }
  }
}