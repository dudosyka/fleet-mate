package com.fleetmate.lib.shared.modules.role

import kotlinx.serialization.Serializable

@Serializable
data class RoleUpdateDto(
    val name: String,
    val description: String
)