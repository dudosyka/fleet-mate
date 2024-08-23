package com.fleetmate.stat.modules.fault.dto


import kotlinx.serialization.Serializable

@Serializable
data class FaultDto (
    val id: Int,
    val carId: Int,
    val carPartId: Int,
    val tripId: Int?,
    val authorId: Int,
    val comment: String,
    val critical: Boolean
)
