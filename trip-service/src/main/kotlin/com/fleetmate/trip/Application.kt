package com.fleetmate.trip

import com.fleetmate.lib.data.model.faults.FaultsModel
import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.data.model.car.CarPartModel
import com.fleetmate.lib.data.model.car.CarPartToCarPartModel
import com.fleetmate.lib.data.model.check.CheckModel
import com.fleetmate.lib.data.model.department.DepartmentModel
import com.fleetmate.lib.data.model.photo.PhotoModel
import com.fleetmate.lib.data.model.position.PositionModel
import com.fleetmate.lib.data.model.trip.TripModel
import com.fleetmate.lib.data.model.user.UserModel
import com.fleetmate.lib.plugins.*
import com.fleetmate.lib.utils.database.DatabaseConnector
import com.fleetmate.lib.utils.kodein.bindSingleton
import com.fleetmate.lib.utils.kodein.kodeinApplication
import com.fleetmate.trip.conf.ServerConf
import com.fleetmate.lib.data.model.car.CarPhotoModel
import com.fleetmate.lib.data.model.car.CarTypeModel
import com.fleetmate.trip.modules.car.controller.CarController
import com.fleetmate.trip.modules.car.service.CarPartService
import com.fleetmate.trip.modules.car.service.CarService
import com.fleetmate.trip.modules.department.service.DepartmentService
import com.fleetmate.lib.utils.files.PhotoService
import com.fleetmate.trip.modules.position.service.PositionService
import com.fleetmate.trip.modules.refuel.controller.RefuelController
import com.fleetmate.trip.modules.refuel.data.model.RefuelModel
import com.fleetmate.trip.modules.refuel.service.RefuelService
import com.fleetmate.trip.modules.report.controller.ReportController
import com.fleetmate.lib.data.model.report.ReportModel
import com.fleetmate.trip.modules.report.service.ReportService
import com.fleetmate.trip.modules.trip.controller.TripController
import com.fleetmate.trip.modules.trip.service.TripService
import com.fleetmate.trip.modules.user.service.UserService
import com.fleetmate.lib.data.model.violation.ViolationModel
import com.fleetmate.trip.modules.bumerang.service.BumerangService
import com.fleetmate.trip.modules.nobilis.service.NobilisService
import com.fleetmate.trip.modules.violation.service.ViolationService
import com.fleetmate.trip.modules.watchdog.WatchDogService
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
//    configureMonitoring()
    install(aesCryptPluginReceive)
    configureSerialization()
    install(aesCryptPluginRespond)
    configureSockets()
    configureExceptionFilter()

    kodeinApplication("/trip") {
        // ----- Services ------
        bindSingleton { TripService(it) }
        bindSingleton { CarService(it) }
        bindSingleton { DepartmentService(it) }
        bindSingleton { PhotoService(it) }
        bindSingleton { PositionService(it) }
        bindSingleton { RefuelService(it) }
        bindSingleton { ReportService(it) }
        bindSingleton { UserService(it) }
        bindSingleton { ViolationService(it) }
        bindSingleton { CarPartService(it) }
        bindSingleton { RefuelController(it) }
        bindSingleton { WatchDogService(it) }
        bindSingleton { BumerangService(it) }
        bindSingleton { NobilisService(it) }
        bindSingleton { PhotoService(it) }

        // ---- Controllers ----
        bindSingleton { TripController(it) }
        bindSingleton { CarController(it) }
        bindSingleton { ReportController(it) }
    }

    DatabaseConnector(
        CarTypeModel,
        CarModel,
        CarPhotoModel,
        CarPartModel,
        CarPartToCarPartModel,
        CheckModel,
        DepartmentModel,
        PhotoModel,
        PositionModel,
        TripModel,
        UserModel,
        RefuelModel,
        ReportModel,
        ViolationModel,
        FaultsModel
    ) {}
}

