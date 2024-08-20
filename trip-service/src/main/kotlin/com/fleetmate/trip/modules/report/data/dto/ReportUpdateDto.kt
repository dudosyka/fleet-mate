package com.fleetmate.trip.modules.report.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReportUpdateDto(
    val mileage: Float?,
    val avgSpeed: Float?,
    val trip: Int?,
    val car: Int?,
    val driver: Int?
)
