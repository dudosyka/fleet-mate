package com.fleetmate.lib.data.dto.role

import kotlinx.serialization.Serializable

@Serializable
data class RoleUpdateDto(
    val name: String,
    val description: String
) {
}