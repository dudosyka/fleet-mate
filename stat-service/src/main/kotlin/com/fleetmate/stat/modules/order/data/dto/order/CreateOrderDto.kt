package com.fleetmate.stat.modules.order.data.dto.order


import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderDto (
    val number: String,
    val mechanicId: Int,
    val faultId: Int
)
