package com.fleetmate.faults.modules.fault.data.dto


import com.fleetmate.lib.shared.modules.photo.data.dto.PhotoUploadDto
import kotlinx.serialization.Serializable

@Serializable
data class FaultInputDto(
    val carId: Int,
    val carPart: Int,
    val photos: List<PhotoUploadDto> = listOf(),
    val comment: String,
)
