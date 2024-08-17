package com.fleetmate.lib.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class CheckUpdateDto(
    val author: Int? = null,
    val startTime: Long? = null,
    val finishTime: Long? = null,
    val timeExceeding: Boolean? = null,
    val automobileId: Int? = null
)