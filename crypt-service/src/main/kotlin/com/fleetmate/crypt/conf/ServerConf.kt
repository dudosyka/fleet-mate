package com.fleetmate.crypt.conf

import com.typesafe.config.ConfigFactory
import io.ktor.server.application.host
import io.ktor.server.application.port
import io.ktor.server.config.HoconApplicationConfig


object ServerConf {
    private val conf = HoconApplicationConfig(ConfigFactory.load().getConfig("server"))
    val host: String = conf.host
    val port: Int = conf.port
}