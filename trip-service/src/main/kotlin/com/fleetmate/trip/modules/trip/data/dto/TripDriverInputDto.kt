package com.fleetmate.trip.modules.trip.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class TripDriverInputDto(
    val driverId: Int,
    val day: Int
) {
}