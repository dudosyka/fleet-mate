package com.fleetmate.trip

import com.fleetmate.lib.model.automobile.AutomobileModel
import com.fleetmate.lib.model.automobile.AutomobileTypeModel
import com.fleetmate.lib.model.check.CheckModel
import com.fleetmate.lib.model.division.DivisionModel
import com.fleetmate.lib.model.photo.PhotoModel
import com.fleetmate.lib.model.post.PostModel
import com.fleetmate.lib.model.trip.TripModel
import com.fleetmate.lib.model.user.UserModel
import com.fleetmate.lib.plugins.*
import com.fleetmate.lib.utils.database.DatabaseConnector
import com.fleetmate.lib.utils.kodein.bindSingleton
import com.fleetmate.lib.utils.kodein.kodeinApplication
import com.fleetmate.trip.conf.ServerConf
import com.fleetmate.trip.modules.automobile.data.model.AutomobilePhotoModel
import com.fleetmate.trip.modules.automobile.data.model.AutomobileToAutomobilePhotoModel
import com.fleetmate.trip.modules.automobile.service.AutomobileService
import com.fleetmate.trip.modules.check.service.CheckService
import com.fleetmate.trip.modules.division.service.DivisionService
import com.fleetmate.trip.modules.photo.service.PhotoService
import com.fleetmate.trip.modules.post.service.PostService
import com.fleetmate.trip.modules.refuel.data.model.RefuelModel
import com.fleetmate.trip.modules.refuel.service.RefuelService
import com.fleetmate.trip.modules.report.data.model.ReportModel
import com.fleetmate.trip.modules.report.service.ReportService
import com.fleetmate.trip.modules.trip.controller.TripController
import com.fleetmate.trip.modules.trip.service.trip.TripService
import com.fleetmate.trip.modules.user.service.UserService
import com.fleetmate.trip.modules.violation.data.model.ViolationModel
import com.fleetmate.trip.modules.violation.data.model.ViolationTypeModel
import com.fleetmate.trip.modules.violation.service.ViolationService
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
        bindSingleton { TripService(it) }
        bindSingleton { AutomobileService(it) }
        bindSingleton { CheckService(it) }
        bindSingleton { DivisionService(it) }
        bindSingleton { PhotoService(it) }
        bindSingleton { PostService(it) }
        bindSingleton { RefuelService(it) }
        bindSingleton { ReportService(it) }
        bindSingleton { UserService(it) }
        bindSingleton { ViolationService(it) }


        // ---- Controllers ----
        bindSingleton { TripController(it) }
    }

    DatabaseConnector(
        AutomobileTypeModel,
        AutomobileModel,
        AutomobilePhotoModel,
        AutomobileToAutomobilePhotoModel,
        CheckModel,
        DivisionModel,
        PhotoModel,
        PostModel,
        TripModel,
        UserModel,
        RefuelModel,
        ReportModel,
        ViolationTypeModel,
        ViolationModel
    ){
//        AutomobileTypeModel.insert {
//            it[name] = "name of type"
//            it[category] = "name of category"
//            it[speedLimit] = "45.5".toString().toFloat()
//            it[speedError] = "0.1".toString().toFloat()
//        }
//        AutomobileModel.insert {
//            it[stateNumber] = "p686pc"
//            it[fuelLevel] = "50.1".toString().toFloat()
//            it[mileage] = "45500".toString().toFloat()
//            it[additionDate] = timestampParam(Date().toInstant())
//            it[type] = 1
//        }
//        PhotoModel.insert {
//            it[link] = "link_field"
//            it[date] = timestampParam(Date().toInstant())
//            it[type] = "some type"
//        }
//        DivisionModel.insert {
//            it[name] = "division_name"
//        }
//        PostModel.insert {
//            it[name] = "post_model"
//        }
//        UserModel.insert {
//            it[fullName] = "Иванов Иван Иванович"
//            it[email] = "test@test.test"
//            it[phoneNumber] = "+79998882221"
//            it[post] = 1
//            it[division] = 1
//        }
//        CheckModel.insert {
//            it[author] = 1
//            it[startTime] = timestampParam(Date().toInstant())
//            it[finishTime] = timestampParam(Date().toInstant())
//            it[timeExceeding] = true
//        }
//        TripModel.insert {
//            it[keyAcceptance] = timestampParam(Date().toInstant())
//            it[status] = "status_field"
//            it[mechanicCheckBeforeTrip] = 1
//            it[driverCheckBeforeTrip] = 1
//            it[mechanicCheckAfterTrip] = 1
//            it[driverCheckAfterTrip] = 1
//            it[keyReturn] = timestampParam(Date().toInstant())
//            it[route] = "route_field"
//            it[speedInfo] = emptyList<Float>()
//            it[avgSpeed] = "120.1".toString().toFloat()
//            it[driver] = 1
//            it[automobile] = 1
//            it[questionable] = false
//            it[needWashing] = false
//            it[washHappen] = false
//        }
//        AutomobilePhotoModel.insert {
//            it[automobile] = 1
//            it[trip] = 1
//            it[driver] = 1
//            it[photo] = 1
//        }
//        AutomobileToAutomobilePhotoModel.insert {
//            it[automobileId] = 1
//            it[automobilePhotoId] = 1
//        }
//        RefuelModel.insert {
//            it[date] = timestampParam(Date().toInstant())
//            it[volume] = "12.1".toString().toFloat()
//            it[automobile] = 1
//            it[trip] = 1
//            it[driver] = 1
//            it[billPhoto] = 1
//        }
//
//        ReportModel.insert {
//            it[mileage] = "456.1".toString().toFloat()
//            it[avgSpeed] = "120.1".toString().toFloat()
//            it[trip] = 1
//            it[automobile] = 1
//            it[driver] = 1
//        }
//        ViolationTypeModel.insert {
//            it[name] = "violation_type_name"
//        }
//
//
//        ViolationModel.insert {
//            it[type] = 1
//            it[date] = timestampParam(Date().toInstant())
//            it[duration] = "45.2".toString().toFloat()
//            it[hidden] = false
//            it[driver] = 1
//            it[trip] = 1
//            it[automobile] = 1
//            it[comment] = "pizdec"
//        }
    }
}