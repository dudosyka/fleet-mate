package com.fleetmate.stat.modules.fault.dto


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.stat.modules.car.dao.CarTypeDao.Companion.stringListCond
import com.fleetmate.stat.modules.car.dto.CarFilterDto
import com.fleetmate.stat.modules.order.data.dto.order.OrderFilterDto
import com.fleetmate.stat.modules.user.dto.filter.StaffFilterDto
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class FaultFilterDto (
    val status: List<Int>? = null,
    val orderFilter: OrderFilterDto? = null,
    val carFilter: CarFilterDto? = null,
    val authorFilter: StaffFilterDto? = null,
) {
    val SqlExpressionBuilder.statusFilterCond: Op<Boolean> get() =
        if (status == null)
            FaultModel.id neq 0
        else
            stringListCond(AppConf.FaultStatus.entries.filter {
                    status.contains(it.id)
                }.map { it.name }, FaultModel.id neq 0, FaultModel.status)
}
