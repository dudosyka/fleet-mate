package com.fleetmate.faults.modules.check.dto


import com.fleetmate.lib.shared.modules.photo.data.dto.PhotoUploadDto
import kotlinx.serialization.Serializable

@Serializable
data class FinishCheckInputDto(
    val carId: Int,
    val needWash: Boolean = false,
    val photos: List<PhotoUploadDto> = listOf(),
    val mileage: Double = 0.0,
    val approved: Boolean = true
)
