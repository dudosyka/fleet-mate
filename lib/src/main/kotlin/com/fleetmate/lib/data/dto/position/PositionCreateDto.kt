package com.fleetmate.lib.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class PositionCreateDto(
    val name : String
) {
}