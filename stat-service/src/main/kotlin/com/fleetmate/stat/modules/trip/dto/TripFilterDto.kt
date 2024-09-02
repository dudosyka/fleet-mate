package com.fleetmate.stat.modules.trip.dto

import com.fleetmate.lib.utils.database.FieldFilterWrapper
import com.fleetmate.stat.modules.car.dto.CarFilterDto
import com.fleetmate.stat.modules.user.dto.filter.StaffFilterDto
import kotlinx.serialization.Serializable

@Serializable
data class TripFilterDto(
    val carFilter: CarFilterDto? = null,
    val startDateRange: FieldFilterWrapper<Long>? = null,
    val endDateRange: FieldFilterWrapper<Long>? = null,
    val driverFilter: StaffFilterDto? = null
)