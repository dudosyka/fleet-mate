package com.fleetmate.faults.modules.check.controller

import com.fleetmate.faults.modules.check.data.dto.CheckFinishInputDto
import com.fleetmate.faults.modules.check.data.dto.CheckStartInputDto
import com.fleetmate.faults.modules.check.data.dto.CheckStartOutputDto
import com.fleetmate.faults.modules.check.service.CheckService
import com.fleetmate.lib.dto.check.CheckOutputDto
import com.fleetmate.lib.utils.kodein.KodeinController
import io.ktor.server.application.call
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
            post("start"){
                val checkStartDto = call.receive<CheckStartInputDto>()
                call.respond<CheckStartOutputDto>(checkService.checkStart(checkStartDto))
            }
            patch("finish") {
                val checkFinishDto = call.receive<CheckFinishInputDto>()
                call.respond<CheckOutputDto>(checkService.checkFinish(checkFinishDto))
            }
        }
    }
}