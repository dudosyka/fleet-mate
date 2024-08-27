package com.fleetmate.lib.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.fleetmate.lib.shared.conf.AppConf.jwt
import com.fleetmate.lib.shared.conf.AppConf.roles
import com.fleetmate.lib.utils.security.jwt.JwtUtil

val jwtVerifier: JWTVerifier = JWT
    .require(Algorithm.HMAC256(jwt.secret))
    .withIssuer(jwt.domain)
    .build()


 fun AuthenticationConfig.loadRole(roleName: String, roleValue: Int) {
     jwt(roleName) {
         verifier(jwtVerifier)
         validate { jwtCredential ->
             val principal = JWTPrincipal(jwtCredential.payload)

             val authorizedUser = JwtUtil.decodeAccessToken(principal)

             if (authorizedUser.roles.any { it.roleId == roleValue})
                 principal
             else
                 null
         }
     }
 }

fun Application.configureSecurity() {
    authentication {
        jwt("default") {
            verifier(jwtVerifier)
            validate {
                JWTPrincipal(it.payload)
            }
        }

        jwt("refresh") {
            verifier(jwtVerifier)
            validate {
                val refresh = it.payload.claims["lastLogin"]

                if (refresh == null)
                    null
                else
                    JWTPrincipal(it.payload)
            }
        }
        loadRole("admin", roles.admin)
        loadRole("driver", roles.driver)
        loadRole("mechanic", roles.mechanic)
        loadRole("washer", roles.washer)
    }
}
