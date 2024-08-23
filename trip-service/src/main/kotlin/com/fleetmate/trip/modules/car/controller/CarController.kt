package com.fleetmate.trip.modules.car.controller


import com.fleetmate.lib.shared.dto.IdInputDto
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.trip.modules.car.service.CarService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class CarController(override val di: DI) : KodeinController() {
    private val carService: CarService by instance()

    override fun Route.registerRoutes() {
        route("car") {
            get("current") {
                val authorizedUser = call.getAuthorized()

                call.respond(carService.getCarByUser(authorizedUser))
            }
            post {
                val authorizedUser = call.getAuthorized()
                val carId = call.receive<IdInputDto>().id

                call.respond(carService.getOne(carId))
            }
        }
    }

}