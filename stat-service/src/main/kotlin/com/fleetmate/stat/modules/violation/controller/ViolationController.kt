package com.fleetmate.stat.modules.violation.controller


import com.fleetmate.lib.utils.kodein.KodeinController
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI

class ViolationController(override val di: DI) : KodeinController() {

    override fun Route.registerRoutes() {
        route("violation") {
            post {
            }
        }
    }

}