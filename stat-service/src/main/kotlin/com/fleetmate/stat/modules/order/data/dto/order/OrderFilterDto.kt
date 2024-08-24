package com.fleetmate.stat.modules.order.data.dto.order


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.dto.StatusDto
import com.fleetmate.lib.utils.database.FieldFilterWrapper
import com.fleetmate.stat.modules.car.dao.CarTypeDao.Companion.likeCond
import com.fleetmate.stat.modules.car.dao.CarTypeDao.Companion.listCond
import com.fleetmate.stat.modules.car.dao.CarTypeDao.Companion.nullableRangeCond
import com.fleetmate.stat.modules.car.dao.CarTypeDao.Companion.rangeCond
import com.fleetmate.stat.modules.order.data.model.OrderModel
import com.fleetmate.stat.modules.user.dto.StaffFilterDto
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and

@Serializable
data class OrderFilterDto (
    val orderNumber: String? = null,
    val status: List<Int>? = null,
    val startDateRange: FieldFilterWrapper<Long>? = null,
    val endDateRange: FieldFilterWrapper<Long>? = null,
    val mechanicFilter: StaffFilterDto,
    val juniorMechanicFilter: StaffFilterDto,
) {

    private fun SqlExpressionBuilder.createStatusFilterCond(statuses: List<Int>?): Op<Boolean> =
        listCond(AppConf.OrderStatus.entries.map { StatusDto(it.id, it.name) }
            .filter {
                statuses?.contains(it.id) ?: false
            }.map { it.name }, OrderModel.id neq 0, OrderModel.status)

    val SqlExpressionBuilder.expressionBuilder: Op<Boolean> get() =
        likeCond(orderNumber, OrderModel.id neq 0, OrderModel.number) and
        createStatusFilterCond(status) and
        rangeCond(startDateRange, OrderModel.id neq 0, OrderModel.startedAt, Long.MIN_VALUE, Long.MAX_VALUE) and
        nullableRangeCond(endDateRange, OrderModel.id neq 0, OrderModel.closedAt, Long.MIN_VALUE, Long.MAX_VALUE) and
        with(mechanicFilter) { expressionBuilder } and
        with(juniorMechanicFilter) { expressionBuilder }
}