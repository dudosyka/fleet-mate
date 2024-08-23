package com.fleetmate.trip.modules.trip.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TripDto (
    val id: Int,
    val createdAt: Long,
    val carId: Int,
    val driverId: Int,
    val status: String,
    val keyAcceptance: Long,
    val needRefuel: Boolean,
)