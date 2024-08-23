package com.fleetmate.crypt.modules.auth.data.dto

data class TokenOutputDto (
    val accessToken: String,
    val refreshToken: String
)