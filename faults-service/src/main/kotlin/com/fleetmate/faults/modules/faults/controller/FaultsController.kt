package com.fleetmate.faults.modules.faults.controller

import com.fleetmate.faults.modules.faults.data.dto.FaultsCreateDto
import com.fleetmate.faults.modules.faults.data.dto.FaultsUpdateDto
import com.fleetmate.faults.modules.faults.service.FaultsService
import com.fleetmate.lib.utils.kodein.KodeinController
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class FaultsController(override val di: DI) : KodeinController() {
    private val faultsService: FaultsService by instance()

    override fun Routing.registerRoutes() {
        get {
            call.respond(faultsService.getAll())
        }
        get("{faultId}") {
            val faultId = call.parameters.getInt("faultId", "Fault ID must be INT")
            call.respond(faultsService.getOne(faultId) ?: throw NotFoundException())
        }
        post {
            val faultsCreateDto = call.receive<FaultsCreateDto>()
            call.respond(faultsService.create(faultsCreateDto))
        }
        patch("{faultId}") {
            val faultId = call.parameters.getInt("faultId", "Fault ID must be INT")
            val faultsUpdateDto = call.receive<FaultsUpdateDto>()
            call.respond(faultsService.update(faultId, faultsUpdateDto))
        }
        delete("{faultId}") {
            val faultId = call.parameters.getInt("faultId", "Fault ID must be INT")
            call.respond(faultsService.delete(faultId))
        }
    }
}