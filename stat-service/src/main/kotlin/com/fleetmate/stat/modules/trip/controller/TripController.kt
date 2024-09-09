package com.fleetmate.stat.modules.trip.controller


import com.fleetmate.lib.shared.dto.IdInputDto
import com.fleetmate.lib.utils.database.FieldFilterWrapper
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.stat.modules.trip.dto.TripFilterDto
import com.fleetmate.stat.modules.trip.service.TripService
import io.ktor.server.application.call
import io.ktor.server.auth.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class TripController(override val di: DI) : KodeinController() {

    private val tripService: TripService by instance()

    override fun Route.registerRoutes() {
        route("trip") {
            authenticate("default") {
                get("status") {
                    call.respond(tripService.getAllStatuses())
                }
            }
            authenticate("admin", "mechanic") {
                route("all") {
                    post {
                        val tripFilterDto = call.receive<TripFilterDto>()
                        // Front send to us the specific value for the start and the end of the trip
                        // we need to create a "fake" window to make it acceptable for rangeConditions
                        tripFilterDto.startDate = with(tripFilterDto.startDate) {
                            if (this == null || specificValue == null)
                                null
                            else
                                FieldFilterWrapper(
                                    bottomBound = specificValue,
                                    topBound = (this.specificValue ?: 0L) + (24 * 60 * 60 * 1000)
                                )
                        }
                        tripFilterDto.endDate = with(tripFilterDto.endDate) {
                            if (this == null || specificValue == null)
                                null
                            else
                                FieldFilterWrapper(
                                    bottomBound = specificValue,
                                    topBound = (this.specificValue ?: 0L) + (24 * 60 * 60 * 1000)
                                )
                        }
                        call.respond(tripService.getAllFiltered(tripFilterDto))
                    }
                    post("driver") {
                        val driverId = call.receive<IdInputDto>().id
                        call.respond(tripService.getByDriver(driverId))
                    }
                    post("car") {
                        val carId = call.receive<IdInputDto>().id
                        call.respond(tripService.getByCar(carId))
                    }
                }
            }
        }
    }

}