package com.fleetmate.lib.shared.modules.car.dto


import kotlinx.serialization.Serializable

@Serializable
data class CarPartDto (
    val id: Int,
    val name: String,
    var children: List<CarPartDto> = listOf()
)
