package com.fleetmate.trip.modules.trip.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TripFinishDto(
    val tripId: Int,
    val carId: Int,
    val mileage: Float
) {
}