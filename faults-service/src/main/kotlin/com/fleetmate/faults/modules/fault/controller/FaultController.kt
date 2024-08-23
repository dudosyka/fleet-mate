package com.fleetmate.faults.modules.fault.controller


import com.fleetmate.faults.modules.fault.data.dto.FaultInputDto
import com.fleetmate.faults.modules.fault.service.FaultService
import com.fleetmate.lib.shared.dto.IdInputDto
import com.fleetmate.lib.utils.kodein.KodeinController
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class FaultController(override val di: DI) : KodeinController() {
    private val faultService: FaultService by instance()

    override fun Route.registerRoutes() {
        post {
            val authorizedUser = call.getAuthorized()
            val faultInputDto = call.receive<FaultInputDto>()

            call.respond(faultService.create(authorizedUser, faultInputDto))
        }

        get("current") {
            val authorizedUser = call.getAuthorized()

            call.respond(faultService.getByUser(authorizedUser))
        }

        post("car") {
            val authorizedUser = call.getAuthorized()
            val carId = call.receive<IdInputDto>().id

            call.respond(faultService.getByCar(authorizedUser, carId))
        }
    }

}