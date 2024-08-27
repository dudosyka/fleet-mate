package com.fleetmate.trip.modules.refuel.controller

import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.trip.modules.refuel.dto.RefuelInputDto
import com.fleetmate.trip.modules.refuel.service.RefuelService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class RefuelController(override val di: DI) : KodeinController() {
    private val refuelService: RefuelService by instance()
    /**
     * Method that subtypes must override to register the handled [Routing] routes.
     */
    override fun Route.registerRoutes() {
        route("refuel") {
            //TODO: driver role
            authenticate("default") {
                post {
                    val authorizedUser = call.getAuthorized()
                    val refuelInputDto = call.receive<RefuelInputDto>()
                    call.respond(refuelService.refuel(authorizedUser.id, refuelInputDto))
                }
            }
        }
    }
}