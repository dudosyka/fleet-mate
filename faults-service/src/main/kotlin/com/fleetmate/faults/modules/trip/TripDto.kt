package com.fleetmate.faults.modules.trip


import kotlinx.serialization.Serializable

@Serializable
data class TripDto (
    val id: Int,
    val carId: Int,
    val driverId: Int,
    val status: String,
    val driverCheckBeforeTrip: Int?,
    val driverCheckAfterTrip: Int?,
    val mechanicCheckBeforeTrip: Int?,
    val mechanicCheckAfterTrip: Int?,
)
