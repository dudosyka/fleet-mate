package com.fleetmate.stat.modules.photo.controller

import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.stat.modules.photo.service.PhotoService
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.kodein.di.DI
import org.kodein.di.instance

class PhotoController(override val di: DI) : KodeinController() {
    private val photoService: PhotoService by instance()

    override fun Route.registerRoutes() {
        route("photo"){
            get("type") {
                call.respond(photoService.getAllTypes())
            }
        }
    }
}