package com.fleetmate.stat.modules.car.dto


import kotlinx.serialization.Serializable

@Serializable
data class CarDto (
    val id: Int,
    val name: String,
    val registrationNumber: String,
    val typeId: Int,
    val fuelLevel: Double,
    val mileage: Double,
    val status: String
)
