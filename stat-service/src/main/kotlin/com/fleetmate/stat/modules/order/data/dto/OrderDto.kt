package com.fleetmate.stat.modules.order.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val id: Int,
    val name: String,
    val status: String,
    val mechanicId: Int,
    val faultId: Int,
    val startedAt: Long,
    val closedAt: Long
)
