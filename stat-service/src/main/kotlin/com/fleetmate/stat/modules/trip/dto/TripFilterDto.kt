package com.fleetmate.stat.modules.trip.dto

import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.utils.database.FieldFilterWrapper
import com.fleetmate.stat.modules.car.dao.CarTypeDao.Companion.nullableRangeCond
import com.fleetmate.stat.modules.car.dao.CarTypeDao.Companion.rangeCond
import com.fleetmate.stat.modules.car.dto.CarFilterDto
import com.fleetmate.stat.modules.user.dto.filter.StaffFilterDto
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and

@Serializable
data class TripFilterDto(
    val carFilter: CarFilterDto? = null,
    val startDateRange: FieldFilterWrapper<Long>? = null,
    val endDateRange: FieldFilterWrapper<Long>? = null,
    val driverFilter: StaffFilterDto? = null
){
    val SqlExpressionBuilder.expressionBuilder: Op<Boolean> get() =
        rangeCond(startDateRange, TripModel.id neq 0, TripModel.keyAcceptance, Long.MIN_VALUE, Long.MAX_VALUE) and
        nullableRangeCond(endDateRange, TripModel.id neq 0, TripModel.keyReturn,  Long.MIN_VALUE, Long.MAX_VALUE) and
        with(carFilter ?: CarFilterDto()) { expressionBuilder } and
        with(driverFilter ?: StaffFilterDto()) { expressionBuilder }
}