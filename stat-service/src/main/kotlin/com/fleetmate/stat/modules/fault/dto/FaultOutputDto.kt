package com.fleetmate.stat.modules.fault.dto

import kotlinx.serialization.Serializable

@Serializable
data class FaultOutputDto(
    val id: Int,
    val createdAt: String,
    val authorName: String,
    val critical: Boolean,
    val carPart: String,
    val status: String,
    val carId: Int,
    val carName: String,
    val orderId: Int? = null,
    val comment: String? = null,
    val photo: List<String>? = null
)