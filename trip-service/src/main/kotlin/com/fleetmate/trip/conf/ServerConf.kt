package com.fleetmate.trip.conf

import com.fleetmate.trip.modules.nobilis.dto.NobilisCredentials
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

object ServerConf {
    private val conf = HoconApplicationConfig(ConfigFactory.load().getConfig("server"))
    private val bumerangConf = HoconApplicationConfig(ConfigFactory.load().getConfig("bumerang"))
    private val
            nobilisConf = HoconApplicationConfig(ConfigFactory.load().getConfig("nobilis"))

//    val bumerangCredentials: BumerangCredentials = BumerangCredentials(
//        bumerangConf.getString("login"), bumerangConf.getString("password")
//    )

    private fun ApplicationConfig.getString(name: String): String = this.property(name).getString()

    val nobilisCredentials: NobilisCredentials = NobilisCredentials(
        nobilisConf.getString("login"), nobilisConf.getString("password")
    )

    val host: String = conf.getString("host")
    val port: Int = conf.getString("port").toInt()
}