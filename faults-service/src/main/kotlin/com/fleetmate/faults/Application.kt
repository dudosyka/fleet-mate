package com.fleetmate.faults

import com.fleetmate.faults.conf.ServerConf
import com.fleetmate.faults.modules.check.controller.CheckController
import com.fleetmate.faults.modules.check.service.CheckService
import com.fleetmate.faults.modules.faults.controller.FaultsController
import com.fleetmate.lib.data.model.faults.FaultsModel
import com.fleetmate.faults.modules.faults.service.FaultsService
import com.fleetmate.faults.modules.photo.service.CarPhotoService
import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.data.model.car.CarPartModel
import com.fleetmate.lib.data.model.car.CarPartToCarPartModel
import com.fleetmate.lib.data.model.car.CarPhotoModel
import com.fleetmate.lib.data.model.car.CarTypeModel
import com.fleetmate.lib.data.model.check.CheckModel
import com.fleetmate.lib.plugins.*
import com.fleetmate.lib.utils.database.DatabaseConnector
import com.fleetmate.lib.utils.files.PhotoService
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
        bindSingleton { FaultsService(it) }
        bindSingleton { CheckService(it) }
        bindSingleton { CarPhotoService(it) }
        bindSingleton { PhotoService(it) }


        // ---- Controllers ----
        bindSingleton { FaultsController(it) }
        bindSingleton { CheckController(it) }
    }

    DatabaseConnector(
        FaultsModel,
        CheckModel,
        CarModel,
        CarTypeModel,
        CarPartModel,
        CarPartToCarPartModel,
        CarPhotoModel,
    ) {

    }
}