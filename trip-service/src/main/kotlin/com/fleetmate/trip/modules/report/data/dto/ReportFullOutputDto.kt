package com.fleetmate.trip.modules.report.data.dto

import com.fleetmate.lib.data.dto.trip.TripFullOutputDto
import kotlinx.serialization.Serializable

@Serializable
data class ReportFullOutputDto(
    val id: Int?,
    val mileage: Double?,
    val avgSpeed: Double?,
    val trip: TripFullOutputDto?
) {
}