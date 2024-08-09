package com.fleetmate.crypt.modules.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenOutputDto (
    val accessToken: String,
    val refreshToken: String? = null
)