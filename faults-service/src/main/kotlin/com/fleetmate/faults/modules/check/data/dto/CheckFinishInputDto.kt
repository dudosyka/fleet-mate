package com.fleetmate.faults.modules.check.data.dto

import com.fleetmate.lib.data.dto.photo.PhotoUploadDto
import kotlinx.serialization.Serializable

@Serializable
data class CheckFinishInputDto(
    val checkId: Int,
    val photos: List<PhotoUploadDto> = listOf(),
    val needWash: Boolean = false,
    val approved: Boolean = true
) {
}