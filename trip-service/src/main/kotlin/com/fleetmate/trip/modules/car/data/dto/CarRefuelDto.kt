package com.fleetmate.trip.modules.car.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CarRefuelDto(
    val need: Boolean?
) {
}