package com.fleetmate.stat.modules.user.dto.filter


import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.stat.modules.order.data.dao.WorkDao.Companion.likeCond
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder

@Serializable
data class StaffFilterDto (
    val fullName: String? = null,
) {
    val SqlExpressionBuilder.expressionBuilder: Op<Boolean> get() =
        likeCond(fullName, UserModel.id neq 0, UserModel.fullName)
}
