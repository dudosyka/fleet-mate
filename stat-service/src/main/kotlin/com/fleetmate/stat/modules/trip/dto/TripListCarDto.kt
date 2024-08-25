package com.fleetmate.stat.modules.trip.dto

import kotlinx.serialization.Serializable

@Serializable
data class TripListCarDto(
    val id: Int,
    val startedAt: Long,
    val finishedAt: Long? = null,
    val driverFullName: String,
    val mileage: Double,
    var violations: Long = 0L
)