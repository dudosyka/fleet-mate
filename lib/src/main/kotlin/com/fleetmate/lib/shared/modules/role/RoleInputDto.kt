package com.fleetmate.lib.shared.modules.role

import kotlinx.serialization.Serializable

@Serializable
data class RoleInputDto(
    val name: String,
    val description: String
)