package com.fleetmate.lib.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class QrTokenDto(
    val userId: Int? = null,
    val carId: Int? = null
) {
}