package com.fleetmate.trip.modules.report.dto


import kotlinx.serialization.Serializable

@Serializable
data class ReportListItemDto (
    val reportId: Int,
    val startAt: Long,
    val finishAt: Long,
    val route: String,
    val car: String,
    val violationCount: Int
)
