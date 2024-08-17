package com.fleetmate.trip.modules.automobile.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AutomobileRefuelDto(
    val need: Boolean?
) {
}