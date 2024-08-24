package com.fleetmate.stat.modules.fault.dto


import com.fleetmate.stat.modules.car.dto.CarFilterDto
import com.fleetmate.stat.modules.order.data.dto.order.OrderFilterDto
import com.fleetmate.stat.modules.user.dto.StaffFilterDto
import kotlinx.serialization.Serializable

@Serializable
data class FaultFilterDto (
    val status: List<Int>? = null,
    val orderFilter: OrderFilterDto? = null,
    val carFilter: CarFilterDto? = null,
    val authorFilter: StaffFilterDto? = null,
)
