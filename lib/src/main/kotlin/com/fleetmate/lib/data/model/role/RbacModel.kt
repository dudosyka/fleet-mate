package com.fleetmate.lib.data.model.role


import com.fleetmate.lib.data.dto.role.LinkedRoleOutputDto
import com.fleetmate.lib.model.user.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
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
            .select(
                query and role.isNotNull() and
                        if (expanded) role.isNotNull() else simplifiedBy.isNull()
            )
            .map {
                LinkedRoleOutputDto(
                    roleId = it[role]!!.value
                )
            }


//        val cols = mutableListOf<Expression<*>>(RoleModel.id, RoleModel.name)
//        RbacModel.leftJoin(RoleModel)
//            .slice(cols)
//            .select {
//                query and role.isNotNull() and
//                        // If expanded we will show ALL rows included which were auto-generated
//                        // So we just put the query-param which won`t change the query res - rule.isNotNull()
//                        if (expanded) role.isNotNull() else simplifiedBy.isNull()
//            }
//            .map {
//                LinkedRoleOutputDto(
//                    roleId = it[role]!!.value
//                )
//            }
    }


    //Expanded param:
    //If it true it means we will select all rules included auto-generated and marked as simplifiedBy
    //If it false we will select only rules which are related to user without auto-generated ones
    fun userToRoleLinks(userId: Int, withStock: Boolean = false, expanded: Boolean = false): List<LinkedRoleOutputDto> =
        getRuleLinks(
            user eq userId and if (expanded) role.isNotNull() else simplifiedBy.isNull() and role.isNull(),
            expanded = expanded
        ).distinctBy { it.roleId }
}

//    fun roleToRuleLinks(roleId: Int, withStock: Boolean = false): List<LinkedRuleOutputDto> =
//        getRuleLinks(
//            role eq roleId and user.isNull(),
//            withStock
//        )
//
//
//    fun unlinkRules(from: Op<Boolean>, linkedRules: List<LinkedRuleInputDto>) = transaction {
//        // Firstly remove rules which are haven`t stock id OR marked as forAll
//        // We can delete them by one query
//        val rulesWithoutStock = linkedRules.filter { it.stockId == null }.map { it.ruleId }
//        RbacModel.deleteWhere {
//            from and (rule inList rulesWithoutStock)
//        }
//
//        // Then start deleting by one item rules which have specified stock id
//        linkedRules.filter { it.stockId != null }.forEach { linkedRule ->
//            RbacModel.deleteWhere {
//                from and (rule eq linkedRule.ruleId) and (stock eq linkedRule.stockId)
//            }
//        }
//    }
//
//    fun unlinkRoles(userId: Int, linkedRoles: List<Int>) = transaction {
//        RbacModel.deleteWhere {
//            (user eq userId) and (role inList linkedRoles)
//        }
//    }
//
//    fun getRelatedUsers(roleId: Int) = RbacModel
//        .leftJoin(UserModel)
//        .slice(id, user, role, stock, rule, simplifiedBy, UserModel.id, UserModel.name)
//        .select {
//            (role eq roleId) and (user.isNotNull())
//        }
//
//    fun expandAppendedRules(roleId: Int, linkedRules: List<LinkedRuleOutputDto>) {
//        val onInsert = getRelatedUsers(roleId).mapNotNull { if (it[user] == null) null else Pair(
//            it[user]!!.value,
//            it[id].value
//        )
//        }.map {
//            Pair(it, linkedRules)
//        }.flatMap { row ->
//            row.second.map { Pair(row.first, it) }
//        }
//
//        RbacModel.batchInsert(onInsert) {
//            this[user] = it.first.first
//            this[rule] = it.second.ruleId
//            this[stock] = it.second.stockId
//            this[simplifiedBy] = it.first.second
//        }
//    }
//
//    fun removeExpandedRules(roleId: Int, linkedRules: List<LinkedRuleInputDto>) {
//        val simplifiedRowsIds = RbacModel.select {
//            (role eq roleId) and (user.isNotNull())
//        }.map { it[id] }
//        linkedRules.map { row ->
//            RbacModel.deleteWhere {
//                (rule eq row.ruleId) and (stock eq row.stockId) and (simplifiedBy inList simplifiedRowsIds)
//            }
//        }
//    }
//
//    fun getUsersRelatedToStock(stockId: Int): List<Int> = transaction {
//        select {
//            stock eq stockId and user.isNotNull()
//        }.map {
//            it[user]!!.value
//        }
//    }
//
//    fun getRelatedToStock(stockId: Int): Pair<Map<Int, List<Int>>, Map<Int, List<Int>>> = transaction {
//        val relatedRoles = mutableMapOf<Int, MutableList<Int>>()
//        val relatedUsers = mutableMapOf<Int, MutableList<Int>>()
//        select {
//            stock eq stockId and simplifiedBy.isNull()
//        }.forEach {
//            val userId = it[user]?.value
//            val roleId = it[role]?.value
//
//            if (
//                (userId != null && roleId != null) ||
//                (userId == null && roleId == null)
//            )
//                return@forEach
//
//            val ruleId = it[rule]?.value ?: return@forEach
//
//            if (roleId != null)
//                if (relatedRoles.containsKey(roleId))
//                    relatedRoles[roleId]!!.add(ruleId)
//                else
//                    relatedRoles[roleId] = mutableListOf(ruleId)
//
//            if (userId != null)
//                if (relatedUsers.containsKey(userId))
//                    relatedUsers[userId]!!.add(ruleId)
//                else
//                    relatedUsers[userId] = mutableListOf(ruleId)
//        }
//
//        Pair(relatedUsers, relatedRoles)
//    }