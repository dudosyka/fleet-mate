package com.fleetmate.trip.modules.trip.controller

import com.fleetmate.lib.data.dto.automobile.AutomobileIdDto
import com.fleetmate.lib.data.dto.trip.TripInitDto
import com.fleetmate.lib.data.dto.trip.TripWashInputDto
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.lib.dto.trip.TripCreateDto
import com.fleetmate.lib.dto.trip.TripUpdateDto
import com.fleetmate.trip.modules.trip.data.dto.TripDriverInputDto
import com.fleetmate.trip.modules.trip.data.dto.TripFinishDto
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
        route("driver"){
            get{
                val tripDriverInputDto = call.receive<TripDriverInputDto>()
                call.respond(tripService.getTripInfo(tripDriverInputDto))
            }
        }
        route("init"){
            post{
                val tripInitDto = call.receive<TripInitDto>()
                call.respond(tripService.initTrip(tripInitDto))
            }
        }
        route("finish"){
            post {
                val tripFinishDto = call.receive<TripFinishDto>()
                call.respond(tripService.finishTrip(tripFinishDto))
            }
        }
        route("wash"){
            route("need"){
                post{
                    val washInputDto = call.receive<TripWashInputDto>()
                    call.respond(tripService.setNeedWash(washInputDto))
                }
            }
            route("complete"){
                post{
                    val washInputDto = call.receive<TripWashInputDto>()
                    call.respond(tripService.setWash(washInputDto))
                }
            }
            route("check"){
                post{
                    val automobileId = call.receive<AutomobileIdDto>()
                    call.respond(tripService.checkWash(automobileId.id))
                }
            }
        }
    }
}