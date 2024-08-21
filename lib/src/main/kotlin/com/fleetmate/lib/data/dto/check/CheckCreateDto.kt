package com.fleetmate.lib.data.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class CheckCreateDto(
    val author: Int,
    val startTime: Long,
    val finishTime: Long? = null,
    val timeExceeded: Boolean? = null,
    val carId: Int
)
