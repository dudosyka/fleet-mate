package com.fleetmate.trip.modules.trip.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class TripInitDto (
    val carId: Int,
    val driverId: Int,
)
