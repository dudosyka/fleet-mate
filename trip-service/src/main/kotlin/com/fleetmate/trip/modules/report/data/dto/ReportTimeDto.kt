package com.fleetmate.trip.modules.report.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReportTimeDto(
    val start: Long,
    val finish: Long
) {
}