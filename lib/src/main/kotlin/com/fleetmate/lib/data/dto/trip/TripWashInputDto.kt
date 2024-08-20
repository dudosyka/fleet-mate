package com.fleetmate.lib.data.dto.trip

import kotlinx.serialization.Serializable

@Serializable
data class TripWashInputDto(
    val carId: Int,
    val wash: Boolean
) {
}