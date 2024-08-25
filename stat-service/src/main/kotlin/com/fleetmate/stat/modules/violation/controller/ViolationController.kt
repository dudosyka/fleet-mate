package com.fleetmate.stat.modules.violation.controller


import com.fleetmate.lib.shared.dto.IdInputDto
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.stat.modules.violation.service.ViolationService
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class ViolationController(override val di: DI) : KodeinController() {

    private val violationService: ViolationService by instance()

    override fun Route.registerRoutes() {
        route("violation") {
            post("all") {
                val tripId = call.receive<IdInputDto>().id
                call.respond(violationService.getByTrip(tripId))
            }
        }
    }

}