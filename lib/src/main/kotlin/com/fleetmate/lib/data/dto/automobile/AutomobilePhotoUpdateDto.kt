package com.fleetmate.lib.data.dto.automobile

import kotlinx.serialization.Serializable

@Serializable
data class AutomobilePhotoUpdateDto(
    val automobile: Int?,
    val trip: Int?,
    val driver: Int?,
    val photo: Int?
)
