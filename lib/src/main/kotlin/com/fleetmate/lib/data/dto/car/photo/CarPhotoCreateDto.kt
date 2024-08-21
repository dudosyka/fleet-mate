package com.fleetmate.lib.data.dto.car.photo

import com.fleetmate.lib.data.dto.photo.PhotoUploadDto
import kotlinx.serialization.Serializable

@Serializable
data class CarPhotoCreateDto(
    val car: Int,
    val trip: Int?,
    val driver: Int,
    val photoList: List<PhotoUploadDto> = listOf()
)
