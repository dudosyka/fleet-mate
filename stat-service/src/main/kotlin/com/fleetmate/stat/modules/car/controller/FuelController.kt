package com.fleetmate.stat.modules.car.controller


import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.stat.modules.car.service.CarService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class FuelController(override val di: DI) : KodeinController() {
    private val carService: CarService by instance()
    override fun Route.registerRoutes() {
        authenticate("default") {
            get("fuels") {
                call.respond(carService.getAllFuelTypes())
            }
        }
    }

}