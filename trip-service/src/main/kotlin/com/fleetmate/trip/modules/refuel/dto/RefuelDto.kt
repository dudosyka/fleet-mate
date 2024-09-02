package com.fleetmate.trip.modules.refuel.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefuelDto(
    val id: Int,
    val volume: Double,
    val trip: Int
)
