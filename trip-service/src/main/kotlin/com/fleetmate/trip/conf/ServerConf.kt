package com.fleetmate.trip.conf

import com.fleetmate.trip.modules.bumerang.dto.BumerangCredentials
import com.fleetmate.trip.modules.nobilis.dto.NobilisCredentials
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*

object ServerConf {
    private val conf = HoconApplicationConfig(ConfigFactory.load().getConfig("server"))
    private val bumerangConf = conf.config("bumerang")
    private val nobilisConf = conf.config("nobilis")
    val host: String = conf.host
    val port: Int = conf.port

    private fun ApplicationConfig.getString(name: String): String = this.property(name).getString()

    val bumerangCredentials: BumerangCredentials = BumerangCredentials(
        bumerangConf.getString("login"), bumerangConf.getString("password")
    )

    val nobilisCredentials: NobilisCredentials = NobilisCredentials(
        nobilisConf.getString("login"), nobilisConf.getString("password")
    )
}