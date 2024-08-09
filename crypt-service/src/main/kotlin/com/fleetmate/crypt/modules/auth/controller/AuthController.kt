package com.fleetmate.crypt.modules.auth.controller

import com.fleetmate.crypt.modules.auth.data.dto.AuthInputDto
import com.fleetmate.crypt.modules.auth.service.AuthService
import com.fleetmate.lib.utils.kodein.KodeinController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance


class AuthController(override val di: DI) : KodeinController() {
    private val authService: AuthService by instance()

    override fun Route.registerRoutes() {

        route("auth") {
            get{
                call.respond("you are here!")
            }
            post {
                val authInput = call.receive<AuthInputDto>()

                call.respond(authService.auth(authInput))
            }

            authenticate("refresh") {
                post("refresh") {
                    val refreshTokenDto = getRefresh(call)

                    call.respond(authService.refreshUser(refreshTokenDto))
                }
            }
            authenticate("default") {
                route("authorized") {
                    get {
                        call.respond(authService.getAuthorized(call.getAuthorized()))
                    }
                }

            }
            authenticate("driver"){
                route("driver"){
                    get{
                        call.respond("driver : success")
                    }
                }
            }
            authenticate("mechanic"){
                route("mechanic"){
                    get{
                        call.respond("mechanic : success")
                    }
                }
            }
            authenticate("admin"){
                route("admin"){
                    get{
                        call.respond("admin : success")
                    }
                }
            }
        }
    }
}
