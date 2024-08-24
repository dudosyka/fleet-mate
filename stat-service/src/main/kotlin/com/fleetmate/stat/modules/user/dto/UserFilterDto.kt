package com.fleetmate.stat.modules.user.dto

import com.fleetmate.lib.utils.database.FieldFilterWrapper
import com.fleetmate.stat.modules.order.data.dto.order.OrderFilterDto
import com.fleetmate.stat.modules.user.dto.filter.StaffFilterDto
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and

@Serializable
data class UserFilterDto (
    val dateRange: FieldFilterWrapper<Long>? = null,
    val staffFilter: StaffFilterDto? = null,
    val orderFilter: OrderFilterDto? = null
) {
    val SqlExpressionBuilder.expressionBuilder: Op<Boolean> get() =
        with(staffFilter ?: StaffFilterDto()) { expressionBuilder } and
        with(orderFilter ?: OrderFilterDto()) { expressionBuilder }
}