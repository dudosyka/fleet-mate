package com.fleetmate.lib.utils.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.jwt.*
import io.ktor.util.date.*
import com.fleetmate.lib.conf.AppConf
import com.fleetmate.lib.data.dto.auth.QrTokenDto
import com.fleetmate.lib.data.dto.role.LinkedRoleOutputDto
import com.fleetmate.lib.data.model.role.RbacModel
import com.fleetmate.lib.exceptions.ForbiddenException
import com.fleetmate.lib.dto.auth.RefreshTokenDto
import com.fleetmate.lib.dto.auth.AuthorizedUser
import com.fleetmate.lib.plugins.Logger
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import java.util.*

typealias EncodedRules = MutableMap<Int, MutableList<Int>>

object JwtUtil {
    private fun encodeRoles(linkedRules: List<LinkedRoleOutputDto>): EncodedRules {
        val encoded = mutableMapOf<Int, MutableList<Int>>()
        linkedRules.forEach {
            encoded[it.roleId] = mutableListOf()
        }

        return encoded
    }

    private fun decodeRules(encoded: EncodedRules): List<LinkedRoleOutputDto> {
        val decoded = mutableListOf<LinkedRoleOutputDto>()
        encoded.forEach { (key, value) ->
            if (value.isEmpty())
                decoded.add(LinkedRoleOutputDto(roleId = key))
            else
                value.forEach { decoded.add(LinkedRoleOutputDto(roleId = key)) }
        }

        return decoded
    }

    fun createToken(userId: Int, lastLogin: Long? = null): String {
        return JWT.create()
            .withIssuer(AppConf.jwt.domain)
            .withIssuedAt(Date(System.currentTimeMillis()))
            .withExpiresAt(
                Date(
                System.currentTimeMillis() +
                        (if (lastLogin != null) AppConf.jwt.refreshExpirationTime else AppConf.jwt.expirationTime) * 1000
                )
            )
            .apply {
                withClaim("id", userId)
                val roles = RbacModel.userToRoleLinks(userId, expanded = true)
                if (lastLogin != null)
                    withClaim("lastLogin", lastLogin)
                else{
                    withClaim("roles", Json.encodeToString(encodeRoles(roles)))
                }

            }.sign(Algorithm.HMAC256(AppConf.jwt.secret))
    }

    fun decodeAccessToken(principal: JWTPrincipal): AuthorizedUser = AuthorizedUser(
        id = principal.getClaim("id", Int::class)!!,
        roles = decodeRules(Json.decodeFromString(principal.getClaim("roles", String::class) ?: "{}"))
    )

    fun decodeRefreshToken(principal: JWTPrincipal): RefreshTokenDto = RefreshTokenDto(
        id = principal.getClaim("id", Int::class)!!,
        lastLogin = principal.getClaim("lastLogin", Long::class)!!
    )

    fun verifyNative(token: String): AuthorizedUser {
        Logger.debug("Verify native", "main")
        val jwtVerifier = JWT
            .require(Algorithm.HMAC256(AppConf.jwt.secret))
            .withIssuer(AppConf.jwt.domain)
            .build()

        val verified = jwtVerifier.verify(token)
        return if (verified != null) {
            val claims = verified.claims
            val currentTime: Long = getTimeMillis() / 1000
            Logger.debug(currentTime, "main")
            Logger.debug(claims["exp"], "main")
            Logger.debug(claims["iss"], "main")
            Logger.debug(claims["id"], "main")
            Logger.debug(claims["rules"], "main")
            if (currentTime > (claims["exp"]?.asInt()
                    ?: 0) || claims["iss"]?.asString() != AppConf.jwt.domain
            ) {
                Logger.debug("expired exception", "main")
                throw ForbiddenException()
            }
            else {
                AuthorizedUser(
                    id = claims["id"]?.asInt() ?: throw ForbiddenException(),
                    roles = decodeRules(Json.decodeFromString(claims["rules"]?.asString() ?: "{}"))
                )
            }
        } else {
            Logger.debug("verified exception", "main")
            throw ForbiddenException()
        }
    }
    fun createMobileAuthToken(qrTokenDto: QrTokenDto): String {
        return JWT.create()
            .withIssuer(AppConf.jwt.domain)
            .withIssuedAt(Date(System.currentTimeMillis()))
            .withExpiresAt(
                Date(System.currentTimeMillis() + AppConf.jwt.expirationTime * 1000)
            )
            .apply {
                withClaim("id", qrTokenDto.userId)
                val roles = RbacModel.userToRoleLinks(qrTokenDto.userId, expanded = true)
                withClaim("roles", Json.encodeToString(encodeRoles(roles)))
            }.sign(Algorithm.HMAC256(AppConf.jwt.secret))
    }

}