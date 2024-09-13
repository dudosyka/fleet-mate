package com.fleetmate.stat.modules.car.dto


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.stat.modules.car.dao.CarTypeDao.Companion.listCond
import com.fleetmate.stat.modules.order.data.dao.WorkDao.Companion.likeCond
import com.fleetmate.stat.modules.violation.dao.ViolationDao.Companion.stringListCond
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and

@Serializable
data class CarFilterDto(
    val id: Int? = null,
    val registrationNumber: String? = null,
    val carType: List<Int>? = null,
    val status: List<Int>? = null
) {
    val SqlExpressionBuilder.expressionBuilder: Op<Boolean> get() =
        (if (id != null)
            CarModel.id eq id
        else
            CarModel.id neq 0) and
        likeCond(registrationNumber, CarModel.id neq 0, CarModel.registrationNumber) and
        listCond(carType, CarModel.id neq 0, CarModel.type) and
        stringListCond(AppConf.CarStatus.entries.filter {
            status?.contains(it.id) ?: false
        }.map { it.name }, CarModel.id neq 0, CarModel.status)

}
