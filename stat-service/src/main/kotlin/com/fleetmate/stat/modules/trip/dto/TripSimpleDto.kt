package com.fleetmate.stat.modules.trip.dto


import kotlinx.serialization.Serializable

@Serializable
data class TripSimpleDto (
    val id: Int,
    val route: String,
    val keyAcceptance: Long,
    val keyReturn: Long
)
