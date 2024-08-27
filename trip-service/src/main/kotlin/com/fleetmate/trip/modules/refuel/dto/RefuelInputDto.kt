package com.fleetmate.trip.modules.refuel.dto

import com.fleetmate.lib.shared.modules.photo.data.dto.PhotoUploadDto
import kotlinx.serialization.Serializable

@Serializable
data class RefuelInputDto(
    val volume: Double,
    val billPhoto: PhotoUploadDto
)
