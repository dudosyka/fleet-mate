package com.fleetmate.file

import com.fleetmate.file.conf.ServerConf
import com.fleetmate.file.modules.stream.StreamController
import com.fleetmate.file.modules.stream.StreamService
import com.fleetmate.lib.plugins.*
import com.fleetmate.lib.utils.database.DatabaseConnector
import com.fleetmate.lib.utils.kodein.bindSingleton
import com.fleetmate.lib.utils.kodein.kodeinApplication
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

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

    kodeinApplication("file") {
        bindSingleton { StreamController(it) }
        bindSingleton { StreamService(it) }
    }

    DatabaseConnector{}
}