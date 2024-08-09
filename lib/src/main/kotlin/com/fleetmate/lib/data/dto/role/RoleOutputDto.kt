package com.fleetmate.lib.data.dto.role

import kotlinx.serialization.Serializable

@Serializable
class RoleOutputDto(
    val id: Int,
    val name: String,
    val description: String?
)