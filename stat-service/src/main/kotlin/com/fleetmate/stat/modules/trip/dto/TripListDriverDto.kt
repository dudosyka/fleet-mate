package com.fleetmate.stat.modules.trip.dto

import com.fleetmate.stat.modules.car.dto.CarSimpleDto
import kotlinx.serialization.Serializable

@Serializable
data class TripListDriverDto(
    val id: Int,
    val startedAt: Long,
    val finishedAt: Long? = null,
    val car: CarSimpleDto,
    val carTypeId: Int,
    val mileage: Double,
    var violations: Long = 0L
)