package com.fleetmate.stat.modules.user.dto


import kotlinx.serialization.Serializable

@Serializable
data class PositionDto(
    val id: Int,
    val name: String
)
