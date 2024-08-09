package com.fleetmate.trip.modules.refuel.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefuelUpdateDto(
    val date: Long? = null,
    val volume: Float? = null,
    val automobile: Int? = null,
    val trip: Int? = null,
    val driver: Int? = null,
    val billPhoto: Int? = null
)
