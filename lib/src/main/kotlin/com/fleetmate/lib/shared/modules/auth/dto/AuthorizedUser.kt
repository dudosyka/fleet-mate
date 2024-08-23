package com.fleetmate.lib.shared.modules.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthorizedUser(
    val id: Int,
)
