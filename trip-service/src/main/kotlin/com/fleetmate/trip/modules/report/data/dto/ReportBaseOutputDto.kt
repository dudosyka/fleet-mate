package com.fleetmate.trip.modules.report.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReportBaseOutputDto(
    val id: Int,
    val date: Long?,
    val faults: Int,
    val car: Int,
    val route: String?
) {
}