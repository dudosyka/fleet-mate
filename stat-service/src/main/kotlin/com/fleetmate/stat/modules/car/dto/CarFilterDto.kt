package com.fleetmate.stat.modules.car.dto


import kotlinx.serialization.Serializable

@Serializable
data class CarFilterDto(
    val registrationNumber: String? = null,
    val carType: List<Int>? = null,
    val status: List<Int>? = null
)
