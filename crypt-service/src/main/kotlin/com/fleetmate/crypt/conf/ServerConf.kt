package com.fleetmate.crypt.conf

import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*

object ServerConf {
    private val conf = HoconApplicationConfig(ConfigFactory.load().getConfig("server"))
    val host: String = conf.host
    val port: Int = conf.port
}