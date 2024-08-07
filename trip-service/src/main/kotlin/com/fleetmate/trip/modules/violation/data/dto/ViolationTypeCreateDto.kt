package com.fleetmate.trip.modules.violation.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ViolationTypeCreateDto(
    val name: String
)
