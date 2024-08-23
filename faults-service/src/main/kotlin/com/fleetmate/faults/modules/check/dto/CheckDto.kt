package com.fleetmate.faults.modules.check.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckDto (
    val id: Int,
    val authorId: Int,
    val carId: Int,
    val startTime: Long,
    val finishTime: Long?,
    val timeExceeded: Boolean
)
