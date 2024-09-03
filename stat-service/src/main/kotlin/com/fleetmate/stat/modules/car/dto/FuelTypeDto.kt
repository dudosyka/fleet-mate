package com.fleetmate.stat.modules.car.dto


import kotlinx.serialization.Serializable

@Serializable
data class FuelTypeDto(
    val id: Int,
    val name: String
)
