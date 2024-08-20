package com.fleetmate.lib.dto.trip

import com.fleetmate.lib.conf.AppConf
import kotlinx.serialization.Serializable

@Serializable
data class TripCreateDto(
    val keyAcceptance: Long,
    val status: AppConf.Status,
    val mechanicCheckBeforeTrip: Int,
    val driverCheckBeforeTrip: Int,
    val mechanicCheckAfterTrip: Int? = null,
    val driverCheckAfterTrip: Int? = null,
    val keyReturn: Long? = null,
    val route: String? = null,
    val speedInfo: List<Float>? = null,
    val avgSpeed: Float? = null,
    val driver: Int,
    val car: Int,
    val questionable: Boolean? = null,
    val needWashing: Boolean? = null,
    val washHappen: Boolean? = null
)
