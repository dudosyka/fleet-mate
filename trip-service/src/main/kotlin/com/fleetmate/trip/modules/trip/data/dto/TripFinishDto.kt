package com.fleetmate.trip.modules.trip.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TripFinishDto(
    val tripId: Int,
    val automobileId: Int,
    val mileage: Float
) {
}