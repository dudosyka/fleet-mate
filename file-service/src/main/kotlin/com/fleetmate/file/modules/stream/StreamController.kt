package com.fleetmate.file.modules.stream


import com.fleetmate.lib.exceptions.BadRequestException
import com.fleetmate.lib.utils.kodein.KodeinController
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class StreamController(override val di: DI) : KodeinController() {
    private val streamService: StreamService by instance()
    override fun Route.registerRoutes() {
        get("id/{id}") {
            val id = call.parameters.getInt("id", "File id must be int")

            call.respondBytes(streamService.getPhotoById(id))
        }
        get("{fileName}") {
            val fileName = call.parameters["fileName"] ?: throw BadRequestException("File name must be string")

            call.respondBytes(streamService.getPhotoByFileName(fileName))
        }
        route("origin") {
            get("id/{id}") {
                val id = call.parameters.getInt("id", "File id must be int")

                call.respondBytes(streamService.getPhotoOriginById(id))
            }
            get("{fileName}") {
                val fileName = call.parameters["fileName"] ?: throw BadRequestException("File name must be string")

                call.respondBytes(streamService.getPhotoOriginByFileName(fileName))
            }
        }
    }

}