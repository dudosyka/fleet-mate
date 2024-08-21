package com.fleetmate.lib.data.dto.violation

import kotlinx.serialization.Serializable

@Serializable
data class ViolationTypeCreateDto(
    val name: String
)
