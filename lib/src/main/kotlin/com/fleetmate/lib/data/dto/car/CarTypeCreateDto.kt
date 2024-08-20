package com.fleetmate.lib.data.dto.car

import com.fleetmate.lib.conf.AppConf
import kotlinx.serialization.Serializable

@Serializable
data class CarTypeCreateDto(
    val name: String,
    val category: AppConf.Category,
    val speedLimit: Float,
    val speedError: Float,
    val avgFuelConsumption: Float
)
