package com.fleetmate.lib.data.dto.faults

import com.fleetmate.lib.conf.AppConf
import com.fleetmate.lib.data.dto.photo.PhotoCreateDto
import kotlinx.serialization.Serializable

@Serializable
data class FaultsCreateDto(
    val tripStatus: AppConf.TripStatus,
    val trip: Int? = null,
    val car: Int,
    val photos: List<PhotoCreateDto> = listOf(),
    val comment: String? = null,
    val carPart: Int
)
