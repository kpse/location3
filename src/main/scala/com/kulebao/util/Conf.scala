package com.kulebao.util

import com.typesafe.config.ConfigFactory

object Conf {
  val config = ConfigFactory.load
  config.checkValid(ConfigFactory.defaultReference)

  val dbUsername = config.getString("location.db.username")
  val dbPassword = config.getString("location.db.password")
  val dbPort = config.getInt("location.db.port")
  val dbName = config.getString("location.db.name")

  val dbPoolMaxObjects = config.getInt("location.db.pool.maxObjects")
  val dbPoolMaxIdle = config.getInt("location.db.pool.maxIdle")
  val dbPoolMaxQueueSize = config.getInt("location.db.pool.maxQueueSize")
}
