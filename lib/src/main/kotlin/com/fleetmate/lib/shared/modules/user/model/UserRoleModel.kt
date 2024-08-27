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
    val user = reference("user_id", UserModel)
    val roleId = integer("role_id")

    val simplifiedBy =
        reference("simplified_by", UserRoleModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE).nullable().default(null)

    fun getRuleLinks(query: Op<Boolean>, expanded: Boolean = false): List<LinkedRoleOutputDto> = transaction {
            UserRoleModel.selectAll()
            .where {
                query and roleId.isNotNull() and
                        if (expanded) roleId.isNotNull() else simplifiedBy.isNull()
            }.map {
                LinkedRoleOutputDto(
                    roleId = it[roleId]
                )
            }
    }

    //Expanded param:
    //If it true it means we will select all rules included auto-generated and marked as simplifiedBy
    //If it false we will select only rules which are related to user without auto-generated ones
    fun userToRoleLinks(userId: Int, expanded: Boolean = false): List<LinkedRoleOutputDto> =
        getRuleLinks(
            user eq userId,
            expanded = expanded
        ).distinctBy { it.roleId }
}