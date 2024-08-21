package com.fleetmate.lib.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenDto (
    val id: Int,
    val lastLogin: Long
)