package com.fleetmate.lib.data.dto.position

import kotlinx.serialization.Serializable

@Serializable
data class PositionCreateDto(
    val name : String
)