package com.fleetmate.stat.modules.trip.dto


import kotlinx.serialization.Serializable

@Serializable
data class TripDto(
    val id: Int,
    val carId: Int,
    val driverId: Int,
    val status: String,
    val route: String,
    val avgSpeed: Double,
    val mileage: Double,
    val keyAcceptance: Long,
    val driverCheckBeforeTripId: Int?,
    val driverCheckAfterTripId: Int?,
    val mechanicCheckBeforeTripId: Int?,
    val mechanicCheckAfterTripId: Int?,
    val keyReturn: Long?,
    val needWashing: Boolean,
    val needRefuel: Boolean
)
