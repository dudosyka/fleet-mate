package com.fleetmate.lib.data.dto.violation

import kotlinx.serialization.Serializable

@Serializable
data class ViolationUpdateDto(
    val date: Long?,
    val type: Int?,
    val duration: Float?,
    val hidden: Boolean?,
    val driver: Int?,
    val trip: Int?,
    val car: Int?,
    val comment: String?
)