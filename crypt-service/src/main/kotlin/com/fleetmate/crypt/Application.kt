package com.fleetmate.crypt

import com.fleetmate.crypt.conf.ServerConf
import com.fleetmate.crypt.modules.access.AccessController
import com.fleetmate.crypt.modules.access.AccessService
import com.fleetmate.crypt.modules.auth.controller.AuthController
import com.fleetmate.crypt.modules.auth.data.model.UserLoginModel
import com.fleetmate.crypt.modules.auth.service.AuthService
import com.fleetmate.crypt.modules.user.service.UserService
import com.fleetmate.lib.plugins.*
import com.fleetmate.lib.shared.conf.DatabaseInitializer
import com.fleetmate.lib.shared.modules.car.model.licence.LicenceTypeModel
import com.fleetmate.lib.shared.modules.department.model.DepartmentModel
import com.fleetmate.lib.shared.modules.position.model.PositionModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.shared.modules.user.model.UserRoleModel
import com.fleetmate.lib.utils.database.DatabaseConnector
import com.fleetmate.lib.utils.kodein.bindSingleton
import com.fleetmate.lib.utils.kodein.kodeinApplication
import com.fleetmate.lib.utils.security.ecdh.ECDH
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    Logger.debug("SERVER CONF: ")
    Logger.debug("${ServerConf.host}:${ServerConf.port}")
    embeddedServer(Netty, port = ServerConf.port, host = ServerConf.host, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureCORS()
    configureMonitoring()
    install(aesCryptPluginReceive)
    configureSerialization()
    install(aesCryptPluginRespond)
    configureExceptionFilter()

    kodeinApplication("/bridge") {
        // ----- Services ------
        bindSingleton { AuthService(it) }
        bindSingleton { UserService(it) }
        bindSingleton { AccessService(it) }

        // ----- Controllers ------
        bindSingleton { AuthController(it) }
        bindSingleton { AccessController(it) }
    }

    DatabaseConnector(
        ECDH, UserModel, UserRoleModel, PositionModel, DepartmentModel, LicenceTypeModel, UserLoginModel
    ) {
        DatabaseInitializer.initUsers()
        commit()
    }
}