package com.fleetmate.lib.data.dto.trip

import com.fleetmate.lib.conf.AppConf
import kotlinx.serialization.Serializable

@Serializable
data class TripUpdateDto(
    val keyAcceptance: Long? = null,
    val status: AppConf.TripStatus? = null,
    val mechanicCheckBeforeTrip: Int? = null,
    val driverCheckBeforeTrip: Int? = null,
    val mechanicCheckAfterTrip: Int? = null,
    val driverCheckAfterTrip: Int? = null,
    val keyReturn: Long? = null,
    val route: String? = null,
    val speedInfo: List<Double>? = null,
    val avgSpeed: Double? = null,
    val driver: Int? = null,
    val car: Int? = null,
    val questionable: Boolean? = null,
    val needWashing: Boolean? = null,
    val washHappen: Boolean? = null
)