package com.fleetmate.stat.modules.user.controller


import com.fleetmate.lib.shared.dto.IdInputDto
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.stat.modules.user.dto.UserFilterDto
import com.fleetmate.stat.modules.user.service.UserService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class UserController(override val di: DI) : KodeinController() {
    private val userService: UserService by instance()
    override fun Route.registerRoutes() {
        authenticate("admin", "mechanic") {
            route("licences") {
                get {
                    call.respond(userService.getLicenceTypes())
                }
            }
            route("staff") {
                get("mechanics") {
                    call.respond(userService.getMechanics())
                }
                get("junior-mechanics") {
                    call.respond(userService.getJuniorMechanics())
                }
                route("all") {
                    post {
                        val userFilterDto = call.receive<UserFilterDto>()

                        call.respond(userService.getStaffFiltered(userFilterDto))
                    }

                    post("xls") {
                        val userFilterDto = call.receive<UserFilterDto>()

                        call.respondBytes(userService.exportStaffToPdf(userFilterDto))
                    }
                }

                post {
                    val staffId = call.receive<IdInputDto>().id
                    val authorizedUser = call.getAuthorized()

                    call.respond(userService.getOneStaff(authorizedUser, staffId))
                }
            }
            route("drivers") {
                post("all") {
                    val userFilterDto = call.receive<UserFilterDto>()

                    call.respond(userService.getDriversFiltered(userFilterDto))
                }

                post {
                    val driverId = call.receive<IdInputDto>().id
                    val authorizedUser = call.getAuthorized()

                    call.respond(userService.getOneDriver(authorizedUser, driverId))
                }
            }
        }
    }

}