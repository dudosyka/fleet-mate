package com.fleetmate.stat.modules.trip.controller


import com.fleetmate.lib.shared.dto.IdInputDto
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.stat.modules.trip.service.CheckService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class CheckController(override val di: DI) : KodeinController() {
    private val checkService: CheckService by instance()
    override fun Route.registerRoutes() {
        route("trip/check") {
            post {
                val checkId = call.receive<IdInputDto>().id

                call.respond(checkService.getOne(checkId))
            }
        }
    }

}