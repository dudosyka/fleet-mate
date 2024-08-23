package com.fleetmate.trip.modules.refuel.dto

import com.fleetmate.lib.shared.modules.photo.data.dto.PhotoUploadDto

data class RefuelInputDto(
    val volume: Double,
    val photoUploadDto: PhotoUploadDto
)
