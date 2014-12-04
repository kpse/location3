package com.kulebao.echo

import java.util.Date

import akka.actor._
import akka.io.Tcp.Write
import akka.util.ByteString
import com.github.mauricio.async.db.RowData
import com.kulebao.CmdPattern
import com.kulebao.db.DB
import com.kulebao.handler.{Handler, HandlerProps}

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
        data match {
          case CmdPattern.cmdV1(id, time, valid, latitude, latitudeD, longitude, longitudeG, speed, direction, date, status) =>
            println(s"V1 cmd")
            val sql: String = "INSERT INTO records (device_id, cmd, time, valid, latitude, latitude_direction, longitude, longitude_direction, speed, direction, date, tracker_status) values " +
              "(?,?,?,?,?,?,?,?,?,?,?,?);"
            execute(sql, id, "V1", time, valid, latitude, latitudeD, longitude, longitudeG, speed, direction, date, status)

          case CmdPattern.cmdNBR(id, time, mcc, mnc, ta, num, groups, lastInGroup, date, status) =>
            println(s"NBR cmd")
            groups.split(",").drop(1).grouped(3).foreach { arr: Array[String] =>
              val sql: String = "INSERT INTO nbr_records (device_id, time, mcc, mnc, ta, num, lac, cid, rxlev, date, tracker_status) values " +
                "(?,?,?,?,?,?,?,?,?,?,?);"
              execute(sql, id, time, mcc, mnc, ta, num, arr(0), arr(1), arr(2), date, status)
            }
          case CmdPattern.cmdLink(id, time, gsm, gps, bat, step, turnover, ex1, ex2, date, status) =>
            println(s"LINK cmd")
            val sql: String = "INSERT INTO link_records (device_id, time, gsm, gps, bat, step, turnover, ex1, ex2, date, tracker_status) values " +
              "(?,?,?,?,?,?,?,?,?,?,?);"
            execute(sql, id, time, gsm, gps, bat, step, turnover, ex1, ex2, date, status)

          case _ =>
            println(s"record to demo: $data")
            log.debug(s"record to demo: $data")
            execute("INSERT INTO demo (data) VALUES (?)", data + "--" + new Date)
        }
      case _ =>
        log.debug("not supported!")
        println("not supported!")
    }
  }
}