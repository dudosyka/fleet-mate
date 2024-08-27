package com.fleetmate.lib.shared.modules.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class QrTokenDto(
    val userId: Int? = null,
    val carId: Int? = null
)