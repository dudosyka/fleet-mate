package com.fleetmate.stat.modules.user.dto.filter


import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.stat.modules.order.data.dao.WorkDao.Companion.likeCond
import com.fleetmate.stat.modules.order.data.dao.WorkDao.Companion.nullableLikeCond
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and

@Serializable
data class StaffFilterDto (
    val id: Int? = null,
    val fullName: String? = null,
    val phoneNumber: String? = null,
    val licenceNumber: String? = null
) {
    val SqlExpressionBuilder.expressionBuilder: Op<Boolean> get() =
        (if (id != null)
            UserModel.id eq id
        else
            UserModel.id neq 0) and
        likeCond(fullName, UserModel.id neq 0, UserModel.fullName) and
        likeCond(phoneNumber, UserModel.id neq 0, UserModel.phoneNumber) and
        nullableLikeCond(licenceNumber, UserModel.id neq 0, UserModel.licenceNumber)
}
