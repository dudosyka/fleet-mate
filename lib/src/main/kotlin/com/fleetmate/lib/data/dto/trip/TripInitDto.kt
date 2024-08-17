package com.fleetmate.lib.data.dto.trip

import kotlinx.serialization.Serializable

@Serializable
data class TripInitDto(
    val driverId: Int,
    val automobileId: Int
) {
}