package com.fleetmate.trip.modules.report.controller


import com.fleetmate.lib.shared.dto.IdInputDto
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.trip.modules.report.service.ReportService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class ReportController(override val di: DI) : KodeinController() {
    private val reportService: ReportService by instance()
    override fun Route.registerRoutes() {
        route("report") {
            //TODO: driver role
            authenticate("default") {
                post("all") {
                    val authorizedUser = call.getAuthorized()

                    call.respond(reportService.getAllForUser(authorizedUser))
                }
                post {
                    val reportId = call.receive<IdInputDto>().id

                    call.respond(reportService.getOne(reportId))
                }
            }
        }
    }

}