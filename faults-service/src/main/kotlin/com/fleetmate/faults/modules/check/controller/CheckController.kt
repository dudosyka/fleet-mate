package com.fleetmate.faults.modules.check.controller

import com.fleetmate.faults.modules.check.data.dto.CheckDriverFinishDto
import com.fleetmate.faults.modules.check.data.dto.CheckFinishInputDto
import com.fleetmate.faults.modules.check.data.dto.CheckStartInputDto
import com.fleetmate.faults.modules.check.data.dto.CheckStartOutputDto
import com.fleetmate.faults.modules.check.service.CheckService
import com.fleetmate.lib.utils.kodein.KodeinController
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.kodein.di.DI
import org.kodein.di.instance

class CheckController(override val di: DI) : KodeinController() {
    private val checkService: CheckService by instance()
    override fun Route.registerRoutes() {

        route("check"){
            authenticate("mechanic"){
                route("mechanic"){
                    route("before"){
                        post("start"){
                            val checkStartDto = call.receive<CheckStartInputDto>()
                            val userId = call.getAuthorized().id
                            call.respond<CheckStartOutputDto>(checkService.mechanicBeforeStart(checkStartDto, userId))
                        }
                        patch("finish") {
                            val checkFinishDto = call.receive<CheckFinishInputDto>()
                            val userId = call.getAuthorized().id
                            call.respond(checkService.mechanicBeforeFinish(checkFinishDto, userId))
                        }
                    }
                    route("after"){
                        post("start"){
                            val checkStartDto = call.receive<CheckStartInputDto>()
                            val userId = call.getAuthorized().id
                            call.respond<CheckStartOutputDto>(checkService.mechanicAfterStart(checkStartDto, userId))
                        }
                        patch("finish") {
                            val checkFinishDto = call.receive<CheckFinishInputDto>()
                            val userId = call.getAuthorized().id
                            call.respond(checkService.mechanicAfterFinish(checkFinishDto, userId))
                        }
                    }
                }
            }
            authenticate("driver"){
                route("driver"){
                    route("before"){
                        post("start"){
                            val checkStartDto = call.receive<CheckStartInputDto>()
                            val userId = call.getAuthorized().id
                            call.respond<CheckStartOutputDto>(checkService.driverBeforeStart(checkStartDto, userId))
                        }
                        patch("finish") {
                            val checkFinishDto = call.receive<CheckFinishInputDto>()
                            val userId = call.getAuthorized().id
                            call.respond(checkService.driverBeforeFinish(checkFinishDto, userId))
                        }
                    }
                    route("after"){
                        post("start"){
                            val checkStartDto = call.receive<CheckStartInputDto>()
                            val userId = call.getAuthorized().id
                            call.respond<CheckStartOutputDto>(checkService.driverAfterStart(checkStartDto, userId))
                        }
                        patch("finish") {
                            val checkDriverFinishDto = call.receive<CheckDriverFinishDto>()
                            val userId = call.getAuthorized().id
                            call.respond(checkService.driverAfterFinish(checkDriverFinishDto, userId))
                        }
                    }
                }
            }
        }
    }
}