package com.fleetmate.lib.data.dto.violation

import com.fleetmate.lib.conf.AppConf
import kotlinx.serialization.Serializable

@Serializable
data class ViolationCreateDto(
    val date: Long,
    val type: AppConf.ViolationType,
    val duration: Double,
    val hidden: Boolean?,
    val driver: Int,
    val trip: Int,
    val car: Int,
    val comment: String?
)
