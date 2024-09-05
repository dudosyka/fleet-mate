package com.fleetmate.stat.modules.user.dto

import com.fleetmate.lib.utils.database.FieldFilterWrapper
import com.fleetmate.stat.modules.user.dto.filter.StaffFilterDto
import kotlinx.serialization.Serializable

@Serializable
data class UserFilterDto (
    val dateRange: FieldFilterWrapper<Long>? = null,
    val staffFilter: StaffFilterDto? = null,
)