package com.fleetmate.stat.modules.order.data.dto.order


import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val id: Int,
    val number: String,
    val status: String,
    val mechanicId: Int,
    val faultId: Int,
    val startedAt: Long,
    val closedAt: Long?
)
