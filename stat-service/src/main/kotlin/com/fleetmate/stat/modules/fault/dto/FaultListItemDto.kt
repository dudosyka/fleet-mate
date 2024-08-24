package com.fleetmate.stat.modules.fault.dto


import com.fleetmate.stat.modules.car.dto.CarSimpleDto
import kotlinx.serialization.Serializable

@Serializable
data class FaultListItemDto (
    val id: Int,
    val orderNumber: String?,
    val status: String,
    val createdAt: String,
    val car: CarSimpleDto,

)
