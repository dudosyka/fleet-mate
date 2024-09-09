package com.fleetmate.stat.modules.order.data.dto.order


import com.fleetmate.stat.modules.car.dto.CarSimpleDto
import com.fleetmate.stat.modules.user.dto.UserSimpleDto
import kotlinx.serialization.Serializable

@Serializable
data class OrderListItemDto (
    val id: Int,
    val orderNumber: String,
    val startedAt: Long,
    val status: String,
    val closedAt: Long? = null,
    val mechanic: UserSimpleDto,
    val hours: Double = 0.0,
    val car: CarSimpleDto
)
