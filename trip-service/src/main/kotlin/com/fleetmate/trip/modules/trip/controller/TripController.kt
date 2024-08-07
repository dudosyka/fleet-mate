package com.fleetmate.trip.modules.trip.controller

import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.lib.dto.trip.TripCreateDto
import com.fleetmate.lib.dto.trip.TripFullOutputDto
import com.fleetmate.lib.dto.trip.TripUpdateDto
import com.fleetmate.trip.modules.trip.service.trip.TripService
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class TripController(override val di: DI) : KodeinController() {
    private val tripService: TripService by instance()

    override fun Route.registerRoutes() {
        get {
            call.respond<TripFullOutputDto>(tripService.getOne(2)!!)
        }
        get("{tripId}") {
            val tripId = call.parameters.getInt("tripId", "Trip ID must be INT")
            call.respond(tripService.getOne(tripId) ?: throw NotFoundException())
        }
        post {
            val tripCreateDto = call.receive<TripCreateDto>()
            call.respond(tripService.create(tripCreateDto))
        }
        patch("{tripId}") {
            val tripId = call.parameters.getInt("tripId", "Trip ID must be INT")
            val tripUpdateDto = call.receive<TripUpdateDto>()
            call.respond(tripService.update(tripId, tripUpdateDto))
        }
        delete("{tripId}") {
            val tripId = call.parameters.getInt("tripId", "Trip ID must be INT")
            call.respond(tripService.delete(tripId))
        }
    }
}