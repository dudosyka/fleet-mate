package com.fleetmate.lib.data.dto.role

import kotlinx.serialization.Serializable

@Serializable
data class RoleInputDto(
    val name: String,
    val description: String
) {
}