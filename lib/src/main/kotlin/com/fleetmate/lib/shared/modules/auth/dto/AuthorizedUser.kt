package com.fleetmate.lib.shared.modules.auth.dto

import com.fleetmate.lib.shared.modules.role.LinkedRoleOutputDto
import kotlinx.serialization.Serializable

@Serializable
data class AuthorizedUser(
    val id: Int,
    val roles: List<LinkedRoleOutputDto>
)
