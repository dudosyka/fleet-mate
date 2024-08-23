package com.fleetmate.stat.modules.car.dto.type


import kotlinx.serialization.Serializable

@Serializable
data class CarTypeDto (
    val id: Int,
    val name: String,
    val speedLimit: Double,
    val speedError: Double,
    val licenceType: Int
)
