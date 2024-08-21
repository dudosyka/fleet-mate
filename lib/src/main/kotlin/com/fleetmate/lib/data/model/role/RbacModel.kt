package com.fleetmate.lib.data.model.role


import com.fleetmate.lib.data.dto.role.LinkedRoleOutputDto
import com.fleetmate.lib.data.model.user.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.collections.distinctBy
import kotlin.collections.map


object RbacModel: BaseIntIdTable() {
    val user = reference("user", UserModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE).nullable().default(null)
    val role = reference(
        "role",
        RoleModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE
    ).nullable().default(null)

    // it links on "main" row which is simplified this
    // field means that current row is created to expand some relations:
    // 1) user-role relation to be the many user-rule relations
    // 2) user-rule-stock and role-rule-stock (forAll) to be the manu user-rule-stocks and role-rule-stocks relations
    val simplifiedBy =
        reference("simplified_by", RbacModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE).nullable().default(null)

    //Future iterations
    // Field is created for user-rule-stock and role-rule-stock (rules with stock) relations
    // it means that user have access to all stocks by this rule ex. can view all stocks
    // For such rule will be automatically generated all user-rule-stocks relations and marked as simplifiedBy this one
    //val forAll = bool("for_all").default(false)

    fun getRuleLinks(query: Op<Boolean>, expanded: Boolean = false): List<LinkedRoleOutputDto> = transaction {
        (RbacModel leftJoin RoleModel)
            .selectAll()
            .where {
                query and role.isNotNull() and
                        if (expanded) role.isNotNull() else simplifiedBy.isNull()
            }
            .map {
                LinkedRoleOutputDto(
                    roleId = it[role]!!.value
                )
            }
    }


    //Expanded param:
    //If it true it means we will select all rules included auto-generated and marked as simplifiedBy
    //If it false we will select only rules which are related to user without auto-generated ones
    fun userToRoleLinks(userId: Int, expanded: Boolean = false): List<LinkedRoleOutputDto> =
        getRuleLinks(
            user eq userId and if (expanded) role.isNotNull() else simplifiedBy.isNull() and role.isNull(),
            expanded = expanded
        ).distinctBy { it.roleId }
}