package com.fleetmate.lib.data.dto.report

import kotlinx.serialization.Serializable

@Serializable
data class ReportCreateDto (
    val mileage: Float,
    val avgSpeed: Float,
    val trip: Int,
    val car: Int,
    val driver: Int
)