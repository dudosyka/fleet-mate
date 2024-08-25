package com.fleetmate.stat.modules.trip.dto

import kotlinx.serialization.Serializable

@Serializable
data class TripViolationListItemDto(
    val id: Int,
    val type: String,
    val registeredAt: Long,
    val duration: Long
)