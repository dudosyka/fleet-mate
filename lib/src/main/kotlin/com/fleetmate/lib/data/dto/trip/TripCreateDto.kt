package com.fleetmate.lib.dto.trip

import kotlinx.serialization.Serializable

@Serializable
data class TripCreateDto(
    val keyAcceptance: Long,
    val status: String,
    val mechanicCheckBeforeTrip: Int,
    val driverCheckBeforeTrip: Int,
    val mechanicCheckAfterTrip: Int? = null,
    val driverCheckAfterTrip: Int? = null,
    val keyReturn: Long? = null,
    val route: String? = null,
    val speedInfo: List<Float>? = null,
    val avgSpeed: Float? = null,
    val driver: Int,
    val automobile: Int,
    val questionable: Boolean? = null,
    val needWashing: Boolean? = null,
    val washHappen: Boolean? = null
)
