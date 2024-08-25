package com.fleetmate.stat.modules.car.dto

import kotlinx.serialization.Serializable

@Serializable
data class CarListItemDto(
    val car: CarSimpleDto,
    val fuelLevel: Double,
    var violations: Int = 0
)