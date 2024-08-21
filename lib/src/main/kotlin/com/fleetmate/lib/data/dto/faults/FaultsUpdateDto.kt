package com.fleetmate.faults.modules.faults.data.dto

import com.fleetmate.lib.conf.AppConf
import kotlinx.serialization.Serializable

@Serializable
data class FaultsUpdateDto(
    val status: AppConf.Status? = null,
    val trip: Int? = null,
    val car: Int? = null,
    val photo: String? = null,
    val comment: String? = null,
    val critical: Boolean? = null
)
