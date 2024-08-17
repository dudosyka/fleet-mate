package com.fleetmate.trip.modules.trip.data.dto

import com.fleetmate.lib.dto.trip.TripOutputDto
import com.fleetmate.trip.modules.automobile.data.dto.AutomobileRefuelDto
import kotlinx.serialization.Serializable

@Serializable
data class TripInitOutputDto(
    val refuel: AutomobileRefuelDto,
    val trip: TripOutputDto
) {
}