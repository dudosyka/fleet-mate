package com.fleetmate.faults

import com.fleetmate.faults.conf.ServerConf
import com.fleetmate.faults.modules.check.controller.CheckController
import com.fleetmate.faults.modules.check.service.CheckService
import com.fleetmate.faults.modules.fault.controller.FaultController
import com.fleetmate.faults.modules.fault.service.FaultService
import com.fleetmate.lib.plugins.*
import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.car.model.part.CarPartModel
import com.fleetmate.lib.shared.modules.check.model.CheckModel
import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.lib.shared.modules.fault.model.FaultPhotoModel
import com.fleetmate.lib.shared.modules.photo.data.model.PhotoModel
import com.fleetmate.lib.shared.modules.photo.service.PhotoService
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
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
    install(aesCryptPluginReceive)
    configureSerialization()
    install(aesCryptPluginRespond)
    configureSockets()
    configureExceptionFilter()

    kodeinApplication("/faults") {
        // ----- Services ------
        bindSingleton { PhotoService(it) }
        bindSingleton { CheckService(it) }
        bindSingleton { FaultService(it) }


        // ---- Controllers ----
        bindSingleton { CheckController(it) }
        bindSingleton { FaultController(it) }
    }

    DatabaseConnector(
        CheckModel, CarModel, UserModel, PhotoModel,
        FaultModel, CarPartModel, FaultPhotoModel, TripModel
    ) {
    }
}