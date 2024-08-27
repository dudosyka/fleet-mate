package com.fleetmate.trip

import com.fleetmate.lib.plugins.*
import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.car.model.part.CarPartModel
import com.fleetmate.lib.shared.modules.car.model.part.CarPartToCarPartModel
import com.fleetmate.lib.shared.modules.car.model.type.CarTypeModel
import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.lib.shared.modules.photo.data.model.PhotoModel
import com.fleetmate.lib.shared.modules.refuel.model.RefuelModel
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.shared.modules.violation.model.ViolationModel
import com.fleetmate.lib.shared.modules.wash.model.WashModel
import com.fleetmate.lib.utils.database.DatabaseConnector
import com.fleetmate.lib.utils.kodein.bindSingleton
import com.fleetmate.lib.utils.kodein.kodeinApplication
import com.fleetmate.trip.conf.ServerConf
import com.fleetmate.trip.modules.car.controller.CarController
import com.fleetmate.trip.modules.car.service.CarService
import com.fleetmate.trip.modules.nobilis.service.NobilisService
import com.fleetmate.trip.modules.refuel.controller.RefuelController
import com.fleetmate.trip.modules.refuel.service.RefuelService
import com.fleetmate.trip.modules.report.controller.ReportController
import com.fleetmate.trip.modules.report.service.ReportService
import com.fleetmate.trip.modules.trip.controller.TripController
import com.fleetmate.trip.modules.trip.service.TripService
import com.fleetmate.trip.modules.user.service.UserService
import com.fleetmate.trip.modules.violation.service.ViolationService
import com.fleetmate.trip.modules.wash.controller.WashController
import com.fleetmate.trip.modules.wash.service.WashService
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

    kodeinApplication("/trip") {
        // ----- Services ------
        bindSingleton { CarService(it) }
        bindSingleton { NobilisService(it) }
        bindSingleton { RefuelService(it) }
        bindSingleton { ReportService(it) }
        bindSingleton { TripService(it) }
        bindSingleton { UserService(it) }
        bindSingleton { ViolationService(it) }
        bindSingleton { WashService(it) }



        // ---- Controllers ----
        bindSingleton { CarController(it) }
        bindSingleton { RefuelController(it) }
        bindSingleton { ReportController(it) }
        bindSingleton { TripController(it) }
        bindSingleton { WashController(it) }
    }

    DatabaseConnector(
        CarModel, CarTypeModel, CarPartModel, CarPartToCarPartModel,
        FaultModel, TripModel, RefuelModel, PhotoModel, UserModel,
        ViolationModel, WashModel
    ) {
    }
}