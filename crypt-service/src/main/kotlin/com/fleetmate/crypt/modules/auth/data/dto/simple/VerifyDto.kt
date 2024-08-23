package com.fleetmate.crypt.modules.auth.data.dto.simple

data class VerifyDto (
    val phone: String,
    val code: String
)