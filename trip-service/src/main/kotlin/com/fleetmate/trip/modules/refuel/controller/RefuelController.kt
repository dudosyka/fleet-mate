package com.fleetmate.trip.modules.refuel.controller

import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.trip.modules.car.service.CarService
import com.fleetmate.trip.modules.refuel.data.dto.RefuelInputDto
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.kodein.di.DI
import org.kodein.di.instance

class RefuelController(override val di: DI) : KodeinController() {

    private val carService: CarService by instance()

    override fun Route.registerRoutes() {
        route("refuel"){
            post {
                val refuelCreateDto = call.receive<RefuelInputDto>()
                call.respond(carService.refuel(refuelCreateDto))
            }
        }
    }
}