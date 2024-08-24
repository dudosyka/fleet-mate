package com.fleetmate.stat.modules.order.data.dto


import com.fleetmate.stat.modules.car.dto.CarSimpleDto
import com.fleetmate.stat.modules.trip.dto.TripSimpleDto
import kotlinx.serialization.Serializable

@Serializable
data class WashDto (
    val id: Int,
    val date: String,
    val car: CarSimpleDto,
    val trip: TripSimpleDto
)
