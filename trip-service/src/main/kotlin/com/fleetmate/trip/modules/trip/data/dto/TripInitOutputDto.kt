package com.fleetmate.trip.modules.trip.data.dto

import com.fleetmate.lib.dto.trip.TripOutputDto
import com.fleetmate.trip.modules.car.data.dto.CarRefuelDto
import kotlinx.serialization.Serializable

@Serializable
data class TripInitOutputDto(
    val refuel: CarRefuelDto,
    val trip: TripOutputDto
) {
}