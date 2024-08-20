package com.fleetmate.lib.data.dto.car

import kotlinx.serialization.Serializable

@Serializable
data class CarUpdateDto(
    val registrationNumber: String? = null,
    val fuelLevel: Float? = null,
    val mileage: Float? = null,
    val dateAdded: Long? = null,
    val type: Int? = null
)
