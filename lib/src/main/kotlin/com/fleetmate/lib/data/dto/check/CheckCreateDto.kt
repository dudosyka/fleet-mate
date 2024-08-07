package com.fleetmate.lib.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class CheckCreateDto(
    val author: Int,
    val startTime: Long,
    val finishTime: Long? = null,
    val timeExceeding: Boolean? = null
)
