package com.fleetmate.lib.data.dto.report

import kotlinx.serialization.Serializable

@Serializable
data class ReportCreateDto (
    val mileage: Double,
    val avgSpeed: Double,
    val trip: Int,
    val car: Int,
    val driver: Int
)