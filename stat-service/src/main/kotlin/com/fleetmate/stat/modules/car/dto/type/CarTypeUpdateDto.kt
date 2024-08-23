package com.fleetmate.stat.modules.car.dto.type


import kotlinx.serialization.Serializable

@Serializable
data class CarTypeUpdateDto(
    val id: Int,
    val speedLimit: Double? = null,
    val speedError: Double? = null,
    val licenceType: Int? = null,
    val name: String? = null
)
