package com.fleetmate.stat.modules.car.controller


import com.fleetmate.lib.shared.dto.IdInputDto
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.stat.modules.car.dto.type.CarTypeUpdateDto
import com.fleetmate.stat.modules.car.service.CarTypeService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class CarTypeController(override val di: DI) : KodeinController() {
    private val carTypeService: CarTypeService by instance()

    override fun Route.registerRoutes() {
        route("car/type") {
            patch {
                val carTypeUpdateDto = call.receive<CarTypeUpdateDto>()

                call.respond(carTypeService.update(carTypeUpdateDto))
            }
            post {
                val carTypeId = call.receive<IdInputDto>().id

                call.respond(carTypeService.getOne(carTypeId))
            }
        }
    }

}