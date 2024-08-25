package com.fleetmate.stat.modules.user.dto


import kotlinx.serialization.Serializable

@Serializable
data class MechanicWorkListItemDto(
    val orderNumber: String,
    val startedAt: Long,
    val hours: Double,
    val faultId: Int,
    val orderId: Int,
    val mechanicFullName: String,
)
