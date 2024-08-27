package com.fleetmate.lib.shared.modules.role

import kotlinx.serialization.Serializable

@Serializable
data class RoleOutputDto(
    val id: Int,
    val name: String,
    val description: String?
)