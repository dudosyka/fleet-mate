package com.fleetmate.trip

import com.fleetmate.faults.modules.faults.data.model.FaultsModel
import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.data.model.car.CarPartModel
import com.fleetmate.lib.data.model.car.CarPartToCarPartModel
import com.fleetmate.lib.model.check.CheckModel
import com.fleetmate.lib.model.division.DepartmentModel
import com.fleetmate.lib.model.photo.PhotoModel
import com.fleetmate.lib.model.post.PositionModel
import com.fleetmate.lib.model.trip.TripModel
import com.fleetmate.lib.model.user.UserModel
import com.fleetmate.lib.plugins.*
import com.fleetmate.lib.utils.database.DatabaseConnector
import com.fleetmate.lib.utils.kodein.bindSingleton
import com.fleetmate.lib.utils.kodein.kodeinApplication
import com.fleetmate.trip.conf.ServerConf
import com.fleetmate.lib.data.model.car.CarPhotoModel
import com.fleetmate.lib.data.model.car.CarToCarPhotoModel
import com.fleetmate.lib.data.model.car.CarTypeModel
import com.fleetmate.lib.utils.security.bcrypt.CryptoUtil
import com.fleetmate.trip.modules.car.controller.CarController
import com.fleetmate.trip.modules.car.service.CarPartService
import com.fleetmate.trip.modules.car.service.CarService
import com.fleetmate.trip.modules.department.service.DepartmentService
import com.fleetmate.trip.modules.photo.service.PhotoService
import com.fleetmate.trip.modules.position.service.PositionService
import com.fleetmate.trip.modules.refuel.controller.RefuelController
import com.fleetmate.trip.modules.refuel.data.model.RefuelModel
import com.fleetmate.trip.modules.refuel.service.RefuelService
import com.fleetmate.trip.modules.report.controller.ReportController
import com.fleetmate.lib.data.model.report.ReportModel
import com.fleetmate.trip.modules.report.service.ReportService
import com.fleetmate.trip.modules.trip.controller.TripController
import com.fleetmate.trip.modules.trip.service.trip.TripService
import com.fleetmate.trip.modules.user.service.UserService
import com.fleetmate.trip.modules.violation.data.model.ViolationModel
import com.fleetmate.trip.modules.violation.data.model.ViolationTypeModel
import com.fleetmate.trip.modules.violation.service.ViolationService
import com.fleetmate.trip.modules.watchdog.WatchDogService
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestampParam
import org.jetbrains.exposed.sql.selectAll
import java.util.Date

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

        // ---- Controllers ----
        bindSingleton { TripController(it) }
        bindSingleton { CarController(it) }
        bindSingleton { ReportController(it) }
    }

    DatabaseConnector(
        CarTypeModel,
        CarModel,
        CarPhotoModel,
        CarToCarPhotoModel,
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
        ViolationTypeModel,
        ViolationModel,
        FaultsModel
    ) {

        if (CarModel.selectAll().empty()) {
            PhotoModel.insert {
                it[id] = 1
                it[link] = "link_field"
                it[date] = timestampParam(Date().toInstant())
                it[type] = "some type"
            }
            DepartmentModel.insert {
                it[id] = 1
                it[name] = "division_name"
            }
            PositionModel.insert {
                it[id] = 1
                it[name] = "post_model"
            }

            UserModel.insert {
                it[id] = 1
                it[login] = "admin"
                it[hash] = CryptoUtil.hash("admin")
                it[fullName] = "Admin User"
                it[email] = "email@email.email"
                it[phoneNumber] = "+79775468521"
                it[position] = 1
                it[department] = 1
            }

            CarTypeModel.insert {
                it[id] = 1
                it[name] = "name of type"
                it[category] = "name of category"
                it[speedLimit] = "45.5".toString().toFloat()
                it[speedError] = "0.1".toString().toFloat()
                it[avgFuelConsumption] = (10).toFloat()
            }
            CarModel.insert {
                it[id] = 1
                it[registrationNumber] = "p686pc"
                it[fuelLevel] = "50.1".toString().toFloat()
                it[mileage] = "45500".toString().toFloat()
                it[dateAdded] = timestampParam(Date().toInstant())
                it[type] = 1
            }


            PhotoModel.insert {
                it[link] = "link_field"
                it[date] = timestampParam(Date().toInstant())
                it[type] = "some type"
            }
            DepartmentModel.insert {
                it[name] = "division_name"
            }
            PositionModel.insert {
                it[name] = "post_model"
            }
            CheckModel.insert {
                it[id] = 1
                it[author] = 1
                it[startTime] = timestampParam(Date().toInstant())
                it[finishTime] = timestampParam(Date().toInstant())
                it[timeExceeded] = true
                it[carId] = 1
            }
            commit()

        }
    }
}

