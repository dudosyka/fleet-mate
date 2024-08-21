package com.fleetmate.trip.modules.refuel.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefuelCreateDto(
    val volume: Double,
    val carId: Int,
    val tripId: Int,
    val driverId: Int,
    val billPhoto: Int
)
