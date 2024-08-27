package com.fleetmate.crypt.modules.auth.data.dto.simple

import kotlinx.serialization.Serializable

@Serializable
data class VerifyDto (
    val phone: String,
    val code: String
)