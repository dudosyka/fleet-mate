package com.fleetmate.lib.data.dto.trip

import kotlinx.serialization.Serializable

@Serializable
data class TripFinishDto(
    val tripId: Int,
    val carId: Int,
    val mileage: Float
) {
}