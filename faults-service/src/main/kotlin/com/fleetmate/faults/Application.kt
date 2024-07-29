package com.fleetmate.faults

import com.fleetmate.faults.conf.ServerConf
import com.fleetmate.faults.modules.faults.controller.FaultsController
import com.fleetmate.faults.modules.faults.data.model.FaultsModel
import com.fleetmate.faults.modules.faults.service.FaultsService
import com.fleetmate.lib.plugins.*
import com.fleetmate.lib.utils.database.DatabaseConnector
import com.fleetmate.lib.utils.kodein.bindSingleton
import com.fleetmate.lib.utils.kodein.kodeinApplication
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
    configureSerialization()
    configureSockets()
    configureExceptionFilter()

    kodeinApplication {
        // ----- Services ------
        bindSingleton { FaultsService(it) }


        // ---- Controllers ----
        bindSingleton { FaultsController(it) }
    }

    DatabaseConnector(
        FaultsModel
    ) {
        FaultsModel.insert {
            it[field] = "fault"
        }
        commit()
    }
}