package com.fleetmate.stat

import com.fleetmate.lib.plugins.*
import com.fleetmate.lib.shared.conf.DatabaseInitializer
import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.car.model.CarPhotoModel
import com.fleetmate.lib.shared.modules.car.model.licence.LicenceTypeModel
import com.fleetmate.lib.shared.modules.car.model.part.CarPartModel
import com.fleetmate.lib.shared.modules.car.model.type.CarTypeModel
import com.fleetmate.lib.shared.modules.department.model.DepartmentModel
import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.lib.shared.modules.photo.data.model.PhotoModel
import com.fleetmate.lib.shared.modules.position.model.PositionModel
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.shared.modules.violation.model.ViolationModel
import com.fleetmate.lib.shared.modules.wash.model.WashModel
import com.fleetmate.lib.utils.database.DatabaseConnector
import com.fleetmate.lib.utils.kodein.bindSingleton
import com.fleetmate.lib.utils.kodein.kodeinApplication
import com.fleetmate.stat.conf.ServerConf
import com.fleetmate.stat.modules.car.controller.CarController
import com.fleetmate.stat.modules.car.controller.CarTypeController
import com.fleetmate.stat.modules.car.service.CarService
import com.fleetmate.stat.modules.car.service.CarTypeService
import com.fleetmate.stat.modules.fault.controller.FaultController
import com.fleetmate.stat.modules.fault.service.FaultService
import com.fleetmate.stat.modules.order.controller.OrderController
import com.fleetmate.stat.modules.order.data.model.OrderModel
import com.fleetmate.stat.modules.order.data.model.WorkActorsModel
import com.fleetmate.stat.modules.order.data.model.WorkModel
import com.fleetmate.stat.modules.order.data.model.WorkTypeModel
import com.fleetmate.stat.modules.order.service.OrderService
import com.fleetmate.stat.modules.photo.controller.PhotoController
import com.fleetmate.stat.modules.photo.service.PhotoService
import com.fleetmate.stat.modules.trip.controller.TripController
import com.fleetmate.stat.modules.trip.service.TripService
import com.fleetmate.stat.modules.user.controller.UserController
import com.fleetmate.stat.modules.user.model.UserHoursModel
import com.fleetmate.stat.modules.user.model.UserPhotoModel
import com.fleetmate.stat.modules.user.service.UserService
import com.fleetmate.stat.modules.violation.controller.ViolationController
import com.fleetmate.stat.modules.violation.service.ViolationService
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

    kodeinApplication("/stat") {
        // ----- Services ------
        bindSingleton { CarService(it) }
        bindSingleton { CarTypeService(it) }
        bindSingleton { FaultService(it) }
        bindSingleton { OrderService(it) }
        bindSingleton { TripService(it) }
        bindSingleton { UserService(it) }
        bindSingleton { ViolationService(it) }
        bindSingleton { PhotoService(it) }




        // ---- Controllers ----
        bindSingleton { CarController(it) }
        bindSingleton { CarTypeController(it) }
        bindSingleton { FaultController(it) }
        bindSingleton { OrderController(it) }
        bindSingleton { TripController(it) }
        bindSingleton { UserController(it) }
        bindSingleton { ViolationController(it) }
        bindSingleton { PhotoController(it) }

    }

    DatabaseConnector(
        CarModel, CarPartModel, CarPhotoModel, FaultModel, PhotoModel,
        TripModel, ViolationModel, CarTypeModel, LicenceTypeModel,
        OrderModel, WorkActorsModel, WorkModel, WorkTypeModel,
        WashModel, UserModel, UserHoursModel, ViolationModel,
        DepartmentModel, PositionModel, UserPhotoModel,
    ) {
        DatabaseInitializer.initCarSubTables()
        DatabaseInitializer.initTripViolations()
    }
}