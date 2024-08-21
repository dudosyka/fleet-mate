package com.fleetmate.trip.modules.bumerang.dto

import kotlinx.serialization.Serializable

@Serializable
data class BumerangCarResponse(
    val licenseNumber: String
)

@Serializable
data class BumerangGetCarsResponse(
    val objs: List<BumerangCarResponse>
)
