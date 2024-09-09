package com.fleetmate.stat.modules.user.dto

import com.fleetmate.lib.utils.database.FieldFilterWrapper
import com.fleetmate.stat.modules.user.dto.filter.StaffFilterDto
import kotlinx.serialization.Serializable

@Serializable
data class UserFilterDto (
    var startDate: FieldFilterWrapper<Long>? = null,
    var endDate: FieldFilterWrapper<Long>? = null,
    val staffFilter: StaffFilterDto? = null,
) {
    fun parseRanges() {
        val rangeSize = (24 * 60 * 60 * 1000L)
        startDate = startDate?.createRangeFromSpecificValue(rangeSize)
        endDate = endDate?.createRangeFromSpecificValue(rangeSize)
    }
}