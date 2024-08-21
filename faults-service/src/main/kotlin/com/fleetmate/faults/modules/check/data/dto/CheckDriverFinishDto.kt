package com.fleetmate.faults.modules.check.data.dto

import com.fleetmate.lib.data.dto.photo.PhotoUploadDto
import kotlinx.serialization.Serializable

@Serializable
data class CheckDriverFinishDto(
    val checkId: Int,
    val photos: List<PhotoUploadDto> = listOf(),
    val needWash: Boolean? = null,
    val mileage: Double
) {
}