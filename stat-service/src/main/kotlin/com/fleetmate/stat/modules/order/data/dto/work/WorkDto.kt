package com.fleetmate.stat.modules.order.data.dto.work


import kotlinx.serialization.Serializable

@Serializable
data class WorkDto(
    val id: Int,
    val workType: Int,
    val actorsIds: List<Int>
)
