package com.fleetmate.stat.modules.fault.dto


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.dto.StatusDto
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
    fun SqlExpressionBuilder.createStatusFilterCond(statuses: List<Int>?): Op<Boolean> =
        stringListCond(AppConf.FaultStatus.entries.map { StatusDto(it.id, it.name) }
            .filter {
                statuses?.contains(it.id) ?: false
            }.map { it.name }, FaultModel.id neq 0, FaultModel.status)
}
