package com.fleetmate.trip.modules.bumerang.dto

import kotlinx.serialization.Serializable

@Serializable
data class BumerangCredentials (
    val login: String,
    val password: String
)