package com.fleetmate.lib.data.dto.car

import kotlinx.serialization.Serializable

@Serializable
data class CarCreateDto(
    val registrationNumber: String,
    val fuelLevel: Double,
    val mileage: Double,
    val additionalDate: Long,
    val type: Int
)
