package com.fleetmate.stat.modules.order.data.dto.order


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.utils.database.FieldFilterWrapper
import com.fleetmate.stat.modules.car.dao.CarTypeDao.Companion.stringListCond
import com.fleetmate.stat.modules.order.data.model.OrderModel
import com.fleetmate.stat.modules.user.dto.filter.StaffFilterDto
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder

@Serializable
data class OrderFilterDto (
    val orderNumber: String? = null,
    val status: List<Int>? = null,
    val startDateRange: FieldFilterWrapper<Long>? = null,
    val endDateRange: FieldFilterWrapper<Long>? = null,
    val mechanicFilter: StaffFilterDto? = null,
    val juniorMechanicFilter: StaffFilterDto? = null,
) {

    val SqlExpressionBuilder.statusFilterCond: Op<Boolean> get() =
        if (status == null)
            OrderModel.id neq 0
        else
            stringListCond(AppConf.OrderStatus.entries.filter {
                    status.contains(it.id)
                }.map { it.name }, OrderModel.id neq 0, OrderModel.status)

}