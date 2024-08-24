package com.fleetmate.stat.modules.fault.controller


import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.stat.modules.fault.service.FaultService
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class FaultController(override val di: DI) : KodeinController() {
    private val faultService: FaultService by instance()

    override fun Route.registerRoutes() {
        route("fault") {
            get("status") {

            }
            post("all") {

            }
            post {

            }
        }
    }

}