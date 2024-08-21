package com.fleetmate.lib.data.dto.car

import kotlinx.serialization.Serializable

@Serializable
data class CarPhotoUpdateDto(
    val car: Int?,
    val trip: Int?,
    val driver: Int?,
    val photo: Int?,
)
