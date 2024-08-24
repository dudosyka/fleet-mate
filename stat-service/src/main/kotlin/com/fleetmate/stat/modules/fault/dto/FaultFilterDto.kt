package com.fleetmate.stat.modules.fault.dto


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.dto.StatusDto
import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.stat.modules.car.dao.CarTypeDao.Companion.listCond
import com.fleetmate.stat.modules.car.dto.CarFilterDto
import com.fleetmate.stat.modules.order.data.dto.order.OrderFilterDto
import com.fleetmate.stat.modules.user.dto.StaffFilterDto
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and

@Serializable
data class FaultFilterDto (
    val status: List<Int>? = null,
    val orderFilter: OrderFilterDto? = null,
    val carFilter: CarFilterDto? = null,
    val authorFilter: StaffFilterDto? = null,
) {
    private fun SqlExpressionBuilder.createStatusFilterCond(statuses: List<Int>?): Op<Boolean> =
        listCond(AppConf.FaultStatus.entries.map { StatusDto(it.id, it.name) }
            .filter {
                statuses?.contains(it.id) ?: false
            }.map { it.name }, FaultModel.id neq 0, FaultModel.status)

    val SqlExpressionBuilder.expressionBuilder: Op<Boolean> get() =
        createStatusFilterCond(status) and
        with(orderFilter ?: OrderFilterDto()) { expressionBuilder } and
        with(carFilter ?: CarFilterDto()) { expressionBuilder } and
        with(authorFilter ?: StaffFilterDto()) { expressionBuilder }

}
