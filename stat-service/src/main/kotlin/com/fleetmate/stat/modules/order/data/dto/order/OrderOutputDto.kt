package com.fleetmate.stat.modules.order.data.dto.order

import com.fleetmate.stat.modules.order.data.dto.work.WorkListItemDto
import com.fleetmate.stat.modules.user.dto.output.StaffDto
import kotlinx.serialization.Serializable

@Serializable
data class OrderOutputDto (
    val id: Int,
    val number: String,
    val mechanic: StaffDto,
    val workList: List<WorkListItemDto>
)