package com.fleetmate.stat.modules.stat.controller

import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.stat.modules.stat.data.dto.StatCreateDto
import com.fleetmate.stat.modules.stat.data.dto.StatUpdateDto
import com.fleetmate.stat.modules.stat.service.StatService
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class StatController(override val di: DI) : KodeinController() {
    private val statService: StatService by instance()

    override fun Route.registerRoutes() {
        get {
            call.respond(statService.getAll())
        }
        get("{statId}") {
            val statId = call.parameters.getInt("statId", "Stat ID must be INT")
            call.respond(statService.getOne(statId) ?: throw NotFoundException())
        }
        post {
            val faultsCreateDto = call.receive<StatCreateDto>()
            call.respond(statService.create(faultsCreateDto))
        }
        patch("{statId}") {
            val statId = call.parameters.getInt("statId", "Stat ID must be INT")
            val faultsUpdateDto = call.receive<StatUpdateDto>()
            call.respond(statService.update(statId, faultsUpdateDto))
        }
        delete("{statId}") {
            val statId = call.parameters.getInt("statId", "Stat ID must be INT")
            call.respond(statService.delete(statId))
        }
    }
}