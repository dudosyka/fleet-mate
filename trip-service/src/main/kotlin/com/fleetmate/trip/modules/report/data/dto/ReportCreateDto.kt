package com.fleetmate.trip.modules.report.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReportCreateDto (
    val mileage: Float,
    val avgSpeed: Float,
    val trip: Int,
    val automobile: Int,
    val driver: Int
)