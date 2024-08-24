package com.fleetmate.lib.shared.conf

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import java.time.ZoneOffset

object AppConf {
    private val mainConfig: ApplicationConfig = HoconApplicationConfig(ConfigFactory.load().getConfig("application"))
    private val jwtConfig: ApplicationConfig = mainConfig.config("jwt")
    private val databaseConfig: ApplicationConfig = mainConfig.config("database")

    val isDebug: Boolean = mainConfig.getString("debug") == "true"
    val needCrypt: Boolean = mainConfig.getString("crypt") == "true"

    private fun ApplicationConfig.getString(name: String): String = this.property(name).getString()
    private fun ApplicationConfig.getInt(name: String): Int = this.getString(name).toInt()

    val zoneOffset: ZoneOffset = ZoneOffset.ofHours(mainConfig.getInt("zoneOffset"))
    val fileLocation: String = mainConfig.getString("file-location")

    val jwt = JwtConf(
        domain = jwtConfig.getString("domain"),
        secret = jwtConfig.getString("secret"),
        expirationTime = jwtConfig.config("expiration").getInt("seconds"),
        refreshExpirationTime = jwtConfig.config("refreshExpiration").getInt("seconds"),
    )

    val database = DatabaseConf(
        url = databaseConfig.getString("url"),
        driver = databaseConfig.getString("driver"),
        user = databaseConfig.getString("user"),
        password = databaseConfig.getString("password")
    )

    enum class TripStatus() {
        INITIALIZED,
        EXPLOITATION,
        CLOSED,
        CLOSED_DUE_TO_FAULT
    }

    enum class PhotoType() {
        REFUEL,
        FAULT,
        CAR
    }

    enum class ViolationType {
        SPEEDING,
        REFUEL,
        WASHING
    }

    enum class OrderStatus(val id: Int) {
        CREATED(1),
        CLOSED(2)
    }

    enum class FaultStatus(val id: Int) {
        CREATED(1),
        UNDER_WORK(2),
        FIXED(3)
    }

    val mechanicPositionId = mainConfig.config("positions").getInt("mechanic")
    val washerPositionId = mainConfig.config("positions").getInt("washer")
    val driverPositionId = mainConfig.config("positions").getInt("driver")

    val washHoursNormalized = 0.5
}