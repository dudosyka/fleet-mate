package com.fleetmate.trip.modules.refuel.data.dto

data class RefuelInputDto(
    val date: Long,
    val volume: Float,
    val car: Int,
    val driver: Int,
    val billPhoto: String
) {
}