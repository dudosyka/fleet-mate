package com.fleetmate.lib.data.dto.trip

import kotlinx.serialization.Serializable

@Serializable
data class TripInitDto(
    val carId: Int
) {
}