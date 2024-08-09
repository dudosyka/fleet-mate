package com.fleetmate.lib.dto.automobile

import kotlinx.serialization.Serializable

@Serializable
data class AutomobileCreateDto(
    val stateNumber: String,
    val fuelLevel: Float,
    val mileage: Float,
    val additionalDate: Long,
    val type: Int
)
