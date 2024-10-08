package com.fleetmate.faults.modules.check.controller

import com.fleetmate.faults.modules.check.dto.FinishCheckInputDto
import com.fleetmate.faults.modules.check.service.CheckService
import com.fleetmate.lib.shared.dto.IdInputDto
import com.fleetmate.lib.shared.modules.auth.dto.AuthorizedUser
import com.fleetmate.lib.utils.kodein.KodeinController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.kodein.di.DI
import org.kodein.di.instance

class CheckController(override val di: DI) : KodeinController() {
    private val checkService: CheckService by instance()

    private suspend fun PipelineContext<Unit, ApplicationCall>.finishCheck(
        finishMethod: (authorizedUser: AuthorizedUser, finishCheckInputDto: FinishCheckInputDto) -> Boolean
    ) {
        val authorizedUser = call.getAuthorized()
        val finishCheckInputDto = call.receive<FinishCheckInputDto>()

        call.respond(finishMethod(authorizedUser, finishCheckInputDto))
    }

    override fun Route.registerRoutes() {
        route("check") {
            authenticate("default") {
                post("start") {
                    val authorizedUser = call.getAuthorized()
                    val carId = call.receive<IdInputDto>().id

                    call.respond(checkService.start(authorizedUser, carId))
                }
            }
            //TODO: mechanic role
            authenticate("default") {
                route("mechanic") {
                    patch("before") {
                        finishCheck(checkService::finishMechanicCheckBeforeTrip)
                    }
                    patch("after") {
                        finishCheck(checkService::finishMechanicCheckAfterTrip)
                    }
                }
            }
            //TODO: driver role
            authenticate("default") {
                route("driver") {
                    patch("before") {
                        finishCheck(checkService::finishDriverCheckBeforeTrip)
                    }
                    patch("after") {
                        finishCheck(checkService::finishDriverCheckAfterTrip)
                    }
                }
            }
        }
    }
}