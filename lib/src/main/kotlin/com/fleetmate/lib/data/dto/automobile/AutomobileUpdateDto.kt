package com.fleetmate.lib.dto.automobile

import kotlinx.serialization.Serializable

@Serializable
data class AutomobileUpdateDto(
    val stateNumber: String? = null,
    val fuelLevel: Float? = null,
    val mileage: Float? = null,
    val additionalDate: Long? = null,
    val type: Int? = null
)
