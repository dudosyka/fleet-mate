package com.fleetmate.crypt.modules.access

import com.fleetmate.lib.plugins.Logger
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.lib.utils.security.ecdh.PointDto
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class AccessController(override val di: DI) : KodeinController() {
    private val accessService: AccessService by instance()

    override fun Route.registerRoutes() {
        get("public") {
            call.respond(accessService.getPublics())
        }
        post("test") {
            val data = call.receive<PointDto>()
            Logger.debug(data)
            data.x = "new_val"
            data.y = "new_val_too"
            call.respond(data)
        }
    }
}