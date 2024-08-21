package com.fleetmate.trip.modules.refuel.controller

import com.fleetmate.lib.data.dto.car.CarIdDto
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.trip.modules.refuel.data.dto.RefuelInputDto
import com.fleetmate.trip.modules.refuel.service.RefuelService
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.kodein.di.DI
import org.kodein.di.instance

class RefuelController(override val di: DI) : KodeinController() {
    private val refuelService: RefuelService by instance()

    override fun Route.registerRoutes() {
        route("refuel"){
            post {
                val refuelInputDto = call.receive<RefuelInputDto>()
                call.respond(refuelService.refuelCar(refuelInputDto))
            }
            post {
                val carId = call.receive<CarIdDto>().id
                call.respond(refuelService.checkRefuelNeed(carId))
            }
        }
    }
}