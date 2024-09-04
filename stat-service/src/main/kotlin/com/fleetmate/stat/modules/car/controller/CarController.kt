package com.fleetmate.stat.modules.car.controller

import com.fleetmate.lib.shared.dto.IdInputDto
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.stat.modules.car.dto.CarCreateDto
import com.fleetmate.stat.modules.car.dto.CarFilterDto
import com.fleetmate.stat.modules.car.dto.CarOutputDto
import com.fleetmate.stat.modules.car.service.CarService
import io.ktor.server.application.call
import io.ktor.server.auth.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class CarController(override val di: DI) : KodeinController() {

    private val carService: CarService by instance()

    override fun Route.registerRoutes() {
        route("car"){
            authenticate("admin", "mechanic") {
                post("all") {
                    val carFilterDto = call.receive<CarFilterDto>()
                    call.respond(carService.getAllFiltered(carFilterDto))
                }

                post("create") {
                    val carCreateDto = call.receive<CarCreateDto>()
                    call.respond(carService.create(carCreateDto))
                }

                post {
                    val carIdDto = call.receive<IdInputDto>()
                    call.respond<CarOutputDto>(carService.getOne(carIdDto.id))
                }
            }
        }
    }
}