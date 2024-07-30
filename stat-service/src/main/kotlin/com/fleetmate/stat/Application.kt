package com.fleetmate.stat

import com.fleetmate.lib.plugins.*
import com.fleetmate.lib.utils.database.DatabaseConnector
import com.fleetmate.lib.utils.kodein.bindSingleton
import com.fleetmate.lib.utils.kodein.kodeinApplication
import com.fleetmate.stat.conf.ServerConf
import com.fleetmate.stat.modules.stat.controller.StatController
import com.fleetmate.stat.modules.stat.data.model.StatModel
import com.fleetmate.stat.modules.stat.service.StatService
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.insert


fun main() {
    embeddedServer(Netty, port = ServerConf.port, host = ServerConf.host, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureCORS()
    configureMonitoring()
    install(aesCryptPluginReceive)
    configureSerialization()
    install(aesCryptPluginRespond)
    configureSockets()
    configureExceptionFilter()

    kodeinApplication("/stat") {
        // ----- Services ------
        bindSingleton { StatService(it) }


        // ---- Controllers ----
        bindSingleton { StatController(it) }
    }

    DatabaseConnector(
        StatModel
    ) {
        StatModel.insert {
            it[field] = "stat"
        }
        commit()
    }
}