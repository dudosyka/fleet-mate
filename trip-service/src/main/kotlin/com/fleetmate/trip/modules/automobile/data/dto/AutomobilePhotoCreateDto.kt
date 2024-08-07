package com.fleetmate.trip.modules.automobile.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AutomobilePhotoCreateDto(
    val automobile: Int,
    val trip: Int,
    val driver: Int,
    val photo: Int
)
