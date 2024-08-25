package com.fleetmate.stat.modules.user.dto.driver

import kotlinx.serialization.Serializable

@Serializable
data class DriverFaultListItemDto(
    val id: Int,
    val orderNumber: String?,
    val status: String,
    val type: String
)