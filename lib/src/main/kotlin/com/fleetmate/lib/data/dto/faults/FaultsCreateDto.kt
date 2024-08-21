package com.fleetmate.faults.modules.faults.data.dto

import com.fleetmate.lib.conf.AppConf
import com.fleetmate.lib.dto.photo.PhotoCreateDto
import kotlinx.serialization.Serializable

@Serializable
data class FaultsCreateDto(
    val status: AppConf.Status,
    val trip: Int?,
    val car: Int,
    val photo: PhotoCreateDto?,
    val comment: String?,
    val carPart: Int
)
