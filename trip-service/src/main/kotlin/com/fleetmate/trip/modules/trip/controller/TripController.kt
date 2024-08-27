package com.fleetmate.trip.modules.trip.controller

import com.fleetmate.lib.shared.dto.IdInputDto
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.trip.modules.trip.data.dto.TripInitDto
import com.fleetmate.trip.modules.trip.service.TripService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class TripController(override val di: DI) : KodeinController() {
    private val tripService: TripService by instance()

    override fun Route.registerRoutes() {
        //TODO: mechanic role
        authenticate("default") {
            post("init") {
                val tripInitDto = call.receive<TripInitDto>()

                call.respond(tripService.initTrip(tripInitDto))
            }
            post("finish") {
                val driverId = call.receive<IdInputDto>().id

                call.respond(tripService.finishTrip(driverId))
            }
        }
    }
}