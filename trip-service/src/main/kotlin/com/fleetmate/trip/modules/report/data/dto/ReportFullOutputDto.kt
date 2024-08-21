package com.fleetmate.trip.modules.report.data.dto

import com.fleetmate.lib.dto.trip.TripFullOutputDto
import kotlinx.serialization.Serializable

@Serializable
data class ReportFullOutputDto(
    val id: Int?,
    val mileage: Float?,
    val avgSpeed: Float?,
    val trip: TripFullOutputDto?
) {
}