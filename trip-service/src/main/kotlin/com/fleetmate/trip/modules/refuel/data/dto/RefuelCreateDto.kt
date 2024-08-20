package com.fleetmate.trip.modules.refuel.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefuelCreateDto(
    val date: Long,
    val volume: Float,
    val car: Int,
    val trip: Int,
    val driver: Int,
    val billPhoto: Int
)
