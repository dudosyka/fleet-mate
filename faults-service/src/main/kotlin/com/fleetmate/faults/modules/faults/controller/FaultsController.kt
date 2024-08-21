package com.fleetmate.faults.modules.faults.controller

import com.fleetmate.faults.modules.faults.service.FaultsService
import com.fleetmate.lib.data.dto.car.CarIdDto
import com.fleetmate.lib.data.dto.faults.FaultDto
import com.fleetmate.lib.utils.kodein.KodeinController
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class FaultsController(override val di: DI) : KodeinController() {
    private val faultsService: FaultsService by instance()

    override fun Route.registerRoutes() {
        route("automobile"){
            get{
                val carId = call.receive<CarIdDto>()
                call.respond<List<FaultDto>>(faultsService.getAllCriticalByCar(carId.id))
            }
        }
    }
}