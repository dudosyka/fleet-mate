package com.fleetmate.stat.modules.violation.dto


import kotlinx.serialization.Serializable

@Serializable
data class ViolationDto(
    val id: Int,
    val type: String,
    val duration: Long,
    val driverId: Int,
    val tripId: Int,
    val carId: Int?,
    val comment: String?
)
