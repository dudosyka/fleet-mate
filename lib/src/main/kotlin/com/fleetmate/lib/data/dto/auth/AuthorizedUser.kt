package com.fleetmate.lib.dto.auth

import com.fleetmate.lib.data.dto.role.LinkedRoleOutputDto
import kotlinx.serialization.Serializable

@Serializable
data class AuthorizedUser(
    val id: Int,
    val roles: List<LinkedRoleOutputDto>
)
