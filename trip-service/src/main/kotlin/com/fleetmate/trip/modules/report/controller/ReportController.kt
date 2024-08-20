package com.fleetmate.trip.modules.report.controller

import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.trip.modules.report.data.dto.ReportCreateDto
import com.fleetmate.trip.modules.report.data.dto.ReportFullOutputDto
import com.fleetmate.trip.modules.report.data.dto.ReportTimeDto
import com.fleetmate.trip.modules.report.service.ReportService
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.kodein.di.DI
import org.kodein.di.instance

class ReportController(override val di: DI) : KodeinController() {
    private val reportService: ReportService by instance()

    override fun Route.registerRoutes() {
        authenticate("driver"){
            route("report"){
                get("all") {
                    val time = call.receive<ReportTimeDto>()
                    call.respond(reportService.getAll(time.start, time.finish, call.getAuthorized()))
                }
                get("{reportId}"){
                    val reportId = call.parameters.getInt("reportId", "Report Id must be Int")
                    call.respond<ReportFullOutputDto>(reportService.getOne(reportId) ?: throw NotFoundException(""))
                }
                post{
                    val reportCreateDto = call.receive<ReportCreateDto>()
                    call.respond(reportService.create(reportCreateDto))
                }
            }
        }
    }
}