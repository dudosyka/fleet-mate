package com.fleetmate.stat.modules.violation.dto

import kotlinx.serialization.Serializable

@Serializable
data class ViolationListTripDto(
    val id: Int,
    val type: String,
    val registeredAt: Long,
    val duration: Long
)