package com.fleetmate.stat.modules.trip.dto.refuel

import kotlinx.serialization.Serializable

@Serializable
data class RefuelDto(
    val id: Int,
    val volume: Double,
    val trip: Int,
    val timestamp: String,
    val billPhoto: String,
)
