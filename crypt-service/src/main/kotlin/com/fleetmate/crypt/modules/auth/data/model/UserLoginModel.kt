package com.fleetmate.crypt.modules.auth.data.model

import com.fleetmate.crypt.modules.auth.data.dto.simple.VerifyDto
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.exceptions.ForbiddenException
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

object UserLoginModel: BaseIntIdTable() {
    val user = reference("user", UserModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val lastLogin = long("last_login").default(0)
    val verify = text("verification_code").default("")

    fun deleteByUserId(userId: Int) {
        deleteWhere {
            user eq userId
        }
    }

    fun updateLastLogin(userId: Int, lastLogin: Long) {
        insert {
            it[user] = userId
            it[UserLoginModel.lastLogin] = lastLogin
        }
    }

    fun setVerificationCode(userId: Int, code: String) {
        insert {
            it[user] = userId
            it[verify] = code
        }
    }

    fun getByVerificationCode(verifyDto: VerifyDto): ResultRow {
        return UserLoginModel.innerJoin(UserModel).select(UserModel.id, UserModel.phoneNumber, verify).where {(verify eq verifyDto.code) and (UserModel.phoneNumber eq verifyDto.phone)}.firstOrNull() ?: throw ForbiddenException()
    }
}