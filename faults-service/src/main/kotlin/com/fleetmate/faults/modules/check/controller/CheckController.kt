package com.fleetmate.faults.modules.check.controller

import com.fleetmate.faults.modules.check.dto.FinishCheckInputDto
import com.fleetmate.faults.modules.check.service.CheckService
import com.fleetmate.lib.shared.dto.IdInputDto
import com.fleetmate.lib.utils.kodein.KodeinController
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.kodein.di.DI
import org.kodein.di.instance

class CheckController(override val di: DI) : KodeinController() {
    private val checkService: CheckService by instance()

    private suspend fun PipelineContext<Unit, ApplicationCall>.finishCheck() {
        val authorizedUser = call.getAuthorized()
        val finishCheckInputDto = call.receive<FinishCheckInputDto>()

        call.respond(checkService.finishMechanicCheckBeforeTrip(authorizedUser, finishCheckInputDto))
    }

    override fun Route.registerRoutes() {
        route("check") {
            post("start") {
                val authorizedUser = call.getAuthorized()
                val carId = call.receive<IdInputDto>().id

                call.respond(checkService.start(authorizedUser, carId))
            }
            route("mechanic") {
                patch("before") {
                    finishCheck()
                }
                patch("after") {
                    finishCheck()
                }
            }
            route("driver") {
                patch("before") {
                    finishCheck()
                }
                patch("after") {
                    finishCheck()
                }
            }
        }
    }
}