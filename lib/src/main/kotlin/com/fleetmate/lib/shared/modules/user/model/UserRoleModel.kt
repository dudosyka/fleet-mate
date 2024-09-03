package com.fleetmate.lib.shared.modules.user.model

import com.fleetmate.lib.shared.modules.role.LinkedRoleOutputDto
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll

object UserRoleModel: BaseIntIdTable() {
    val user = reference("user_id", UserModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val role = integer("role_id")


    private fun getRoleLinks(query: Op<Boolean>): List<LinkedRoleOutputDto> = transaction {
            UserRoleModel.selectAll()
            .where {
                query and role.isNotNull()
            }.map {
                LinkedRoleOutputDto(
                    roleId = it[role]
                )
            }
    }

    fun userToRoleLinks(userId: Int): List<LinkedRoleOutputDto> =
        getRoleLinks(
            user eq userId,
        ).distinctBy { it.roleId }
}