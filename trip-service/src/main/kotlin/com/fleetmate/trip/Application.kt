package com.fleetmate.trip

import com.fleetmate.lib.plugins.*
import com.fleetmate.lib.utils.database.DatabaseConnector
import com.fleetmate.lib.utils.kodein.bindSingleton
import com.fleetmate.lib.utils.kodein.kodeinApplication
import com.fleetmate.trip.conf.ServerConf
import com.fleetmate.trip.modules.trip.controller.TripController
import com.fleetmate.trip.modules.trip.data.model.TripModel
import com.fleetmate.trip.modules.trip.service.TripService
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
        bindSingleton { TripService(it) }


        // ---- Controllers ----
        bindSingleton { TripController(it) }
    }

    DatabaseConnector(
        TripModel
    ) {
        TripModel.insert {
            it[field] = "trip"
        }
        commit()
    }
}