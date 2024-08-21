package com.fleetmate.lib.data.dto.faults

import com.fleetmate.lib.conf.AppConf
import kotlinx.serialization.Serializable

@Serializable
data class FaultsUpdateDto(
    val tripStatus: AppConf.TripStatus? = null,
    val trip: Int? = null,
    val car: Int? = null,
    val photo: String? = null,
    val comment: String? = null,
    val critical: Boolean? = null
)
