package com.fleetmate.trip.modules.car.data.dto


import com.fleetmate.lib.shared.modules.car.dto.CarPartDto
import kotlinx.serialization.Serializable

@Serializable
data class CarFullDto (
    val id: Int,
    val licenceType: Int,
    val fuelLevel: Double,
    val mileage: Double,
    val carParts: List<CarPartDto> = listOf()
)
