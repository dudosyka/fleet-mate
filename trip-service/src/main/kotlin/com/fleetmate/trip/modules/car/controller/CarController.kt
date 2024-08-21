package com.fleetmate.trip.modules.car.controller

import com.fleetmate.lib.data.dto.car.CarIdDto
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.trip.modules.car.service.CarService
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.kodein.di.DI
import org.kodein.di.instance

class CarController(override val di: DI) : KodeinController() {
    private val carService: CarService by instance()

    override fun Route.registerRoutes() {
        route("car"){
            get{
                val carId = call.receive<CarIdDto>()
                call.respond(carService.getFullInfo(carId.id))
            }
        }
    }
}