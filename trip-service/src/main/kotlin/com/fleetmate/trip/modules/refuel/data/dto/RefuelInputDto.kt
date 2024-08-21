package com.fleetmate.trip.modules.refuel.data.dto

data class RefuelInputDto(
    val date: Long,
    val volume: Double,
    val car: Int,
    val driver: Int,
    val billPhoto: String,
    val billPhotoName: String
)