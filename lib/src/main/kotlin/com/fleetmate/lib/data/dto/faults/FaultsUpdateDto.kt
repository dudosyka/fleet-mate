package com.fleetmate.faults.modules.faults.data.dto

import com.fleetmate.lib.conf.AppConf
import kotlinx.serialization.Serializable

@Serializable
data class FaultsUpdateDto(
    val id: Int,
    val status: AppConf.Status?,
    val trip: Int?,
    val car: Int?,
    val photo: String?,
    val comment: String?,
    val critical: Boolean?
)
