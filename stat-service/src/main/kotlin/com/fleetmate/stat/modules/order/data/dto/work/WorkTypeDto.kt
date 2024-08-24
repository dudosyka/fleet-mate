package com.fleetmate.stat.modules.order.data.dto.work


import kotlinx.serialization.Serializable

@Serializable
data class WorkTypeDto (
    val id: Int,
    val name: String,
    val hours: Double
)
