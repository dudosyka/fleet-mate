package com.fleetmate.crypt.modules.auth.controller

import com.fleetmate.crypt.modules.auth.data.dto.AdminAuthInputDto
import com.fleetmate.crypt.modules.auth.data.dto.simple.SimpleAuthInputDto
import com.fleetmate.crypt.modules.auth.data.dto.simple.VerifyDto
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
    /**
     * Method that subtypes must override to register the handled [Routing] routes.
     */
    override fun Route.registerRoutes() {
        route("auth") {
            post("admin") {
                val adminAuthInputDto = call.receive<AdminAuthInputDto>()
                call.respond(authService.authAdmin(adminAuthInputDto))
            }
            route("simple") {
                post {
                    val simpleAuthInputDto = call.receive<SimpleAuthInputDto>()
                    call.respond(authService.simpleAuth(simpleAuthInputDto))
                }
                post("verify") {
                    val verifyDto = call.receive<VerifyDto>()
                    call.respond(authService.verifySimple(verifyDto))
                }
            }
            authenticate("default") {
                post("refresh") {
                    val refreshTokenDto = call.getRefresh()

                    call.respond(authService.refreshUser(refreshTokenDto))
                }
                get("authorized") {
                    call.respond(call.getAuthorized())
                }
            }
        }

    }
}