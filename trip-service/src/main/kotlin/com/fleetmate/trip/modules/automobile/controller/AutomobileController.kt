package com.fleetmate.trip.modules.automobile.controller

import com.fleetmate.lib.data.dto.automobile.AutomobileIdDto
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.trip.modules.automobile.service.AutomobileService
import com.fleetmate.trip.modules.refuel.data.dto.RefuelInputDto
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.kodein.di.DI
import org.kodein.di.instance

class AutomobileController(override val di: DI) : KodeinController() {
    private val automobileService: AutomobileService by instance()

    override fun Route.registerRoutes() {
        route("automobile"){
            get{
                val automobile = call.receive<AutomobileIdDto>()
                call.respond(automobileService.getFullInfo(automobile.id))
            }
            route("fuel"){
                route("check"){
                    get{
                        val automobileId = call.receive<AutomobileIdDto>()
                        call.respond(automobileService.checkFuel(automobileId.id))
                    }
                }
                post {
                    val refuelCreateDto = call.receive<RefuelInputDto>()
                    call.respond(automobileService.refuel(refuelCreateDto))
                }
            }
        }
    }
}