package com.fleetmate.stat.modules.order.data.dto.work


import kotlinx.serialization.Serializable

@Serializable
data class WorkActorDto (
    val id: Int,
    val fullName: String
)
