package com.fleetmate.lib.data.dto.trip

import kotlinx.serialization.Serializable

@Serializable
data class TripWashInputDto(
    val automobileId: Int,
    val wash: Boolean
) {
}