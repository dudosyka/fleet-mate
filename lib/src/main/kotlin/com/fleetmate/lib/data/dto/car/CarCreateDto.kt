package com.fleetmate.lib.data.dto.car

import kotlinx.serialization.Serializable

@Serializable
data class CarCreateDto(
    val registrationNumber: String,
    val fuelLevel: Float,
    val mileage: Float,
    val additionalDate: Long,
    val type: Int
)
