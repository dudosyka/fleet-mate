package com.fleetmate.stat.modules.trip.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class CheckDto (
    val id: Int,
    val authorId: Int,
    val photos: List<String>
)