package com.fleetmate.trip.modules.wash.controller

import com.fleetmate.lib.shared.dto.IdInputDto
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.trip.modules.wash.service.WashService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class WashController(override val di: DI) : KodeinController() {
    private val washService: WashService by instance()
    /**
     * Method that subtypes must override to register the handled [Routing] routes.
     */
    override fun Route.registerRoutes() {
        route("wash") {
            authenticate {
                post {
                    val authorizedUser = call.getAuthorized()
                    val carId = call.receive<IdInputDto>().id

                    call.respond(washService.registerWash(authorizedUser, carId))
                }
            }
        }
    }
}