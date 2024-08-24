package com.fleetmate.stat.modules.order.data.dto.work


import kotlinx.serialization.Serializable

@Serializable
data class CreateWorkDto (
    val orderId: Int,
    val workType: Int,
    val actors: List<Int>,
)
