package com.fleetmate.crypt.modules.auth.data.dto.simple

import kotlinx.serialization.Serializable

@Serializable
data class SimpleAuthInputDto (
    val phone: String
)