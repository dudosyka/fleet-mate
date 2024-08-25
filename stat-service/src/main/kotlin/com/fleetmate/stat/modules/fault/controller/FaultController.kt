package com.fleetmate.stat.modules.fault.controller


import com.fleetmate.lib.shared.dto.IdInputDto
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.stat.modules.fault.dto.FaultFilterDto
import com.fleetmate.stat.modules.fault.service.FaultService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class FaultController(override val di: DI) : KodeinController() {
    private val faultService: FaultService by instance()

    override fun Route.registerRoutes() {
        route("fault") {
            get("status") {
                call.respond(faultService.getAllStatuses())
            }

            route("all") {
                post {
                    val faultFilterDto = call.receive<FaultFilterDto>()

                    call.respond(faultService.getAllFiltered(faultFilterDto))
                }
                post("driver") {
                    val driverId = call.receive<IdInputDto>().id
                    call.respond(faultService.getByDriver(driverId))
                }
            }
            post {
                val faultId = call.receive<IdInputDto>().id

                call.respond(faultService.getOne(faultId))
            }
        }
    }

}