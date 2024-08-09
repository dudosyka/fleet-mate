package com.fleetmate.crypt.modules.auth.service

import com.fleetmate.crypt.modules.auth.data.dto.AuthInputDto
import com.fleetmate.crypt.modules.auth.data.dto.TokenOutputDto
import com.fleetmate.crypt.modules.auth.data.models.UserLoginModel
import com.fleetmate.crypt.modules.user.service.UserService
import com.fleetmate.lib.dto.auth.AuthorizedUser
import com.fleetmate.lib.dto.auth.RefreshTokenDto
import com.fleetmate.lib.exceptions.ForbiddenException
import com.fleetmate.lib.exceptions.UnauthorizedException
import com.fleetmate.lib.model.user.UserModel
import com.fleetmate.lib.plugins.Logger
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.lib.utils.security.bcrypt.CryptoUtil
import com.fleetmate.lib.utils.security.jwt.JwtUtil
import com.fleetmate.lib.dto.user.UserOutputDto


import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.instance

class AuthService(override val di: DI) : KodeinService(di) {

    val userService: UserService by instance()

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

    fun auth(authInputDto: AuthInputDto): TokenOutputDto = transaction {
        val search = UserModel.select(UserModel.id, UserModel.login, UserModel.hash)
        val user = if (search.empty())
            throw UnauthorizedException()
        else
            search.first()

        if (!CryptoUtil.compare(authInputDto.password, user[UserModel.hash]))
            throw ForbiddenException()

        val userId = user[UserModel.id].value
        val lastLogin = getTimeMillis()
        updateUserLastLogin(userId, lastLogin)

        val tokenPair = generateTokenPair(userId, lastLogin)
        commit()
        tokenPair
    }

    fun getAuthorized(authorizedUser: AuthorizedUser): UserOutputDto {
        val userDto = userService.getOne(authorizedUser.id)
        if (userDto == null){
            return UserOutputDto()
        }
        return userDto
    }
}