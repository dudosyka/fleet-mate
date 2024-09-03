package com.fleetmate.trip.modules.wash.dto

import kotlinx.serialization.Serializable

@Serializable
data class WashDto (
    val id: Int,
    val tripId: Int,
    val authorId: Int,
    val timestamp: Long,
)