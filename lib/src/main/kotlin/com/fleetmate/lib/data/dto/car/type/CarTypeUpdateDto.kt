package com.fleetmate.lib.data.dto.car.type

import com.fleetmate.lib.conf.AppConf
import kotlinx.serialization.Serializable

@Serializable
data class CarTypeUpdateDto(
    val name: String?,
    val category: AppConf.Category?,
    val speedLimit: Double?,
    val speedError: Double?,
    val avgFuelConsumption: Double?
)
