package com.fleetmate.stat.modules.trip.dto

import com.fleetmate.stat.modules.car.dto.CarSimpleDto
import com.fleetmate.stat.modules.user.dto.UserSimpleDto
import kotlinx.serialization.Serializable

@Serializable
class TripListItemDto(
    val id: Int,
    val status: String,
    val startedAt: Long,
    val finishedAt: Long? = null,
    val driver: UserSimpleDto,
    val car: CarSimpleDto,
    var violations: Long = 0L
)