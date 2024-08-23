package com.fleetmate.trip.modules.nobilis.dto

import kotlinx.serialization.Serializable

@Serializable
data class NobilisCredentials(
    val login: String,
    val password: String
)
