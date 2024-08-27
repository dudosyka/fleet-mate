package com.fleetmate.lib.utils.kodein

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import org.kodein.di.DIAware
import org.kodein.di.instance
import com.fleetmate.lib.exceptions.BadRequestException
import com.fleetmate.lib.exceptions.ForbiddenException
import com.fleetmate.lib.shared.modules.auth.dto.RefreshTokenDto
import com.fleetmate.lib.shared.modules.auth.dto.AuthorizedUser
import com.fleetmate.lib.utils.security.jwt.JwtUtil

/**
 * A [KodeinAware] base class for Controllers handling routes.
 * It allows to easily get dependencies, and offers some useful extensions.
 */
@Suppress("KDocUnresolvedReference")
abstract class KodeinController() : DIAware {
    /**
     * Injected dependency with the current [Application].
     */
    val application: Application by instance()

    /**
     * Method that subtypes must override to register the handled [Routing] routes.
     */
    abstract fun Route.registerRoutes()

    fun ApplicationCall.getAuthorized(): AuthorizedUser {
        val principal = principal<JWTPrincipal>() ?: throw ForbiddenException()
        return JwtUtil.decodeAccessToken(principal)
    }

    fun ApplicationCall.getRefresh(): RefreshTokenDto {
        val principal = principal<JWTPrincipal>() ?: throw ForbiddenException()
        return JwtUtil.decodeRefreshToken(principal)
    }

    fun Parameters.getInt(name: String, errorMsg: String): Int {
        val param = this[name] ?: throw BadRequestException(errorMsg)
        return try {
            param.toInt()
        } catch (e: NumberFormatException) {
            throw BadRequestException(errorMsg)
        }
    }
}