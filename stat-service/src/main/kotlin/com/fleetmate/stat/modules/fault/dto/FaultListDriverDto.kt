package com.fleetmate.stat.modules.fault.dto

import kotlinx.serialization.Serializable

@Serializable
data class FaultListDriverDto(
    val id: Int,
    val orderNumber: String?,
    val status: String,
    val type: String
)