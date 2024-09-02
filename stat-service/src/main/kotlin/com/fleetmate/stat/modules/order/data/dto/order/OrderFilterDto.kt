package com.fleetmate.stat.modules.order.data.dto.order


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.dto.StatusDto
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

    fun SqlExpressionBuilder.createStatusFilterCond(statuses: List<Int>?): Op<Boolean> =
        stringListCond(AppConf.OrderStatus.entries.map { StatusDto(it.id, it.name) }
            .filter {
                statuses?.contains(it.id) ?: false
            }.map { it.name }, OrderModel.id neq 0, OrderModel.status)

}