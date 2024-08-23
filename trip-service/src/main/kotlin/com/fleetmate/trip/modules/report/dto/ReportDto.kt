package com.fleetmate.trip.modules.report.dto


import com.fleetmate.trip.modules.violation.dto.ViolationDto
import kotlinx.serialization.Serializable

@Serializable
data class ReportDto (
    val reportId: Int,
    val startAt: Long,
    val finishAt: Long,
    val route: String,
    val car: String,
    val violations: List<ViolationDto>,
    val avgSpeed: Double,
)
