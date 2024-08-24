package com.fleetmate.stat.modules.car.dto


import kotlinx.serialization.Serializable

@Serializable
data class CarSimpleDto (
    val id: Int,
    val name: String,
    val typeName: String,
    val registrationNumber: String
)
