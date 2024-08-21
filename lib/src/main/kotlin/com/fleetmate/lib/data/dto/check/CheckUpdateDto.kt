package com.fleetmate.lib.data.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class CheckUpdateDto(
    val author: Int? = null,
    val startTime: Long? = null,
    val finishTime: Long? = null,
    val timeExceeded: Boolean? = null,
    val carId: Int? = null
)