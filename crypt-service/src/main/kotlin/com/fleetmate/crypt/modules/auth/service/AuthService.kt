package com.fleetmate.crypt.modules.auth.service

import com.fleetmate.crypt.modules.auth.data.UserDao
import com.fleetmate.crypt.modules.auth.data.dto.AdminAuthInputDto
import com.fleetmate.crypt.modules.auth.data.dto.simple.SimpleAuthInputDto
import com.fleetmate.crypt.modules.auth.data.dto.TokenOutputDto
import com.fleetmate.crypt.modules.auth.data.dto.simple.VerifyDto
import com.fleetmate.crypt.modules.auth.data.model.UserLoginModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.shared.modules.auth.dto.RefreshTokenDto
import com.fleetmate.lib.exceptions.ForbiddenException
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.plugins.Logger
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.lib.utils.security.bcrypt.CryptoUtil
import com.fleetmate.lib.utils.security.jwt.JwtUtil
import io.ktor.util.date.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI

class AuthService(override val di: DI) : KodeinService(di) {
    private data class RefreshEvent(
        val userId: Int,
        val newLastLogin: Long
    )


    @OptIn(ObsoleteCoroutinesApi::class)
    private val userRefreshChannel = CoroutineScope(Job()).actor<RefreshEvent>(capacity = Channel.BUFFERED) {
        for (event in this) {
            updateUserLastLogin(event.userId, event.newLastLogin)
        }
    }

    private fun generateTokenPair(userId: Int, refreshTime: Long): TokenOutputDto {
        val accessToken = JwtUtil.createToken(userId)
        val refreshToken = JwtUtil.createToken(userId, lastLogin = refreshTime)

        return TokenOutputDto(accessToken, refreshToken)
    }

    private fun updateUserLastLogin(userId: Int, lastLogin: Long) = transaction {
        UserLoginModel.deleteByUserId(userId)

        UserLoginModel.updateLastLogin(userId, lastLogin)

        commit()
    }

    fun refreshUser(refreshTokenDto: RefreshTokenDto): TokenOutputDto {
        return try {
            val user = transaction {
                val selected = UserModel.select(UserModel.id).where {
                    UserModel.id eq refreshTokenDto.id
                }

                if (selected.count() == 0L)
                    throw ForbiddenException()

                selected.first()[UserModel.id].value
            }
            val newLastLogin = getTimeMillis()
            userRefreshChannel.trySend(RefreshEvent(user, newLastLogin))
            val tokenPair = generateTokenPair(user, newLastLogin)
            tokenPair
        } catch (e: Exception) {
            Logger.debugException("Exception during refresh", e, "main")
            throw ForbiddenException()
        }
    }

    fun authAdmin(adminAuthInputDto: AdminAuthInputDto): TokenOutputDto = transaction {
        val user = try {
            UserModel.getByLogin(adminAuthInputDto.login)
        } catch (_: NotFoundException) {
            throw ForbiddenException()
        }

        if (!CryptoUtil.compare(adminAuthInputDto.password, user[UserModel.hash]))
            throw ForbiddenException()

        val userId = user[UserModel.id].value
        val lastLogin = getTimeMillis()
        updateUserLastLogin(userId, lastLogin)

        val tokenPair = generateTokenPair(userId, lastLogin)
        commit()
        tokenPair
    }

    fun simpleAuth(simpleAuthInputDto: SimpleAuthInputDto): Boolean = transaction {
        val user = try {
            UserModel.getByLogin(simpleAuthInputDto.phone)
        } catch (_: NotFoundException) {
            throw ForbiddenException()
        }

        //TODO: verification code sending
        val code = "1234"

        UserLoginModel.setVerificationCode(user[UserModel.id].value, code)

        true
    }

    fun verifySimple(verifyDto: VerifyDto): TokenOutputDto = transaction {
        val userId = UserLoginModel.getByVerificationCode(verifyDto)[UserModel.id].value

        val lastLogin = getTimeMillis()
        updateUserLastLogin(userId, lastLogin)

        val tokenPair = generateTokenPair(userId, lastLogin)
        commit()
        tokenPair
    }

    fun test_entity_exception(id: Int) = transaction {
        val userDao = UserDao[id]

        userDao.toOutputDto()
    }
}