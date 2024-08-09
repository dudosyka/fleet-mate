package com.fleetmate.crypt.modules.auth.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class AuthInputDto (
    val login: String,
    val password: String
)