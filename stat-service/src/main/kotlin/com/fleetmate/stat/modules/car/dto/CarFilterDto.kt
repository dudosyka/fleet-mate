package com.fleetmate.stat.modules.car.dto


import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.stat.modules.car.dao.CarTypeDao.Companion.listCond
import com.fleetmate.stat.modules.order.data.dao.WorkDao.Companion.likeCond
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and

@Serializable
data class CarFilterDto(
    val registrationNumber: String? = null,
    val carType: List<Int>? = null,
    val status: List<Int>? = null
) {
    val SqlExpressionBuilder.expressionBuilder: Op<Boolean> get() =
        likeCond(registrationNumber, CarModel.id neq 0, CarModel.registrationNumber) and
        listCond(carType, CarModel.id neq 0, CarModel.type)
}
