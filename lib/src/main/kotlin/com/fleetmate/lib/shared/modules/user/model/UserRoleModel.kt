package com.fleetmate.lib.shared.modules.user.model

import com.fleetmate.lib.shared.modules.role.LinkedRoleOutputDto
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll

object UserRoleModel: BaseIntIdTable() {
    val user = reference("user_id", UserModel)
    val roleId = integer("role_id")


    fun getRoleLinks(query: Op<Boolean>): List<LinkedRoleOutputDto> = transaction {
            UserRoleModel.selectAll()
            .where {
                query and roleId.isNotNull()
            }.map {
                LinkedRoleOutputDto(
                    roleId = it[roleId]
                )
            }
    }

    fun userToRoleLinks(userId: Int): List<LinkedRoleOutputDto> =
        getRoleLinks(
            user eq userId,
        ).distinctBy { it.roleId }
}