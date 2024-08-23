package com.fleetmate.trip.modules.nobilis.dto

import kotlinx.serialization.Serializable

@Serializable
data class NobilisAuthResponse(
    val id_token: String
)
