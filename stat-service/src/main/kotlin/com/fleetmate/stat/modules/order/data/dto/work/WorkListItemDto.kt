package com.fleetmate.stat.modules.order.data.dto.work

import kotlinx.serialization.Serializable

@Serializable
data class WorkListItemDto (
    val id: Int,
    val type: String,
    val actors: List<String>,
    val hours: Double
)