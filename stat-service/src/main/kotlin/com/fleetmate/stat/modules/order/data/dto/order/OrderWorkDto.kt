package com.fleetmate.stat.modules.order.data.dto.order


import com.fleetmate.stat.modules.order.data.dto.work.WorkActorDto
import com.fleetmate.stat.modules.order.data.dto.work.WorkTypeDto
import kotlinx.serialization.Serializable

@Serializable
data class OrderWorkDto (
    val workType: WorkTypeDto,
    val actors: List<WorkActorDto>
)
