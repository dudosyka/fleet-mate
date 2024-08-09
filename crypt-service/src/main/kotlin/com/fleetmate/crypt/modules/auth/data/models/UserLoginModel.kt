package com.fleetmate.crypt.modules.auth.data.models

import com.fleetmate.lib.model.user.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object UserLoginModel: BaseIntIdTable() {
    val userId = reference("user_id", UserModel)
    val lastLogin = long("last_login")

    fun deleteByUserId(userId: Int) = transaction {
        UserLoginModel.deleteWhere{ UserLoginModel.userId eq userId}
    }

    fun updateLastLogin(userId : Int, lastLogin: Long) = transaction{
        UserLoginModel.insert {
            it[UserLoginModel.userId] = userId
            it[UserLoginModel.lastLogin] = lastLogin
        }
        commit()
    }

}