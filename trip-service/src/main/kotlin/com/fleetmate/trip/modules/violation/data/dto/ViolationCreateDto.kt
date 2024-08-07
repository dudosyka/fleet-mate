package com.fleetmate.trip.modules.violation.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ViolationCreateDto(
    val date: Long,
    val type: Int,
    val duration: Float,
    val hidden: Boolean?,
    val driver: Int,
    val trip: Int,
    val automobile: Int,
    val comment: String?
)
