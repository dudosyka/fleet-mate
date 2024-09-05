package com.fleetmate.lib.shared.conf

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import java.time.ZoneOffset

object AppConf {
    private val mainConfig: ApplicationConfig = HoconApplicationConfig(ConfigFactory.load().getConfig("application"))
    private val jwtConfig: ApplicationConfig = mainConfig.config("jwt")
    private val databaseConfig: ApplicationConfig = mainConfig.config("database")
    private val rolesConf: ApplicationConfig = mainConfig.config("roles")
    private val positionsConf: ApplicationConfig = mainConfig.config("positions")

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

    val roles = RolesConf(
        admin = rolesConf.getInt("admin"),
        mechanic = rolesConf.getInt("mechanic"),
        juniorMechanic = rolesConf.getInt("junior_mechanic"),
        driver = rolesConf.getInt("driver"),
        washer = rolesConf.getInt("washer")
    )

    enum class TripStatus(val id: Int) {
        INITIALIZED(1),
        EXPLOITATION(2),
        CLOSED(3),
        CLOSED_DUE_TO_FAULT(4)
    }

    enum class PhotoType(val id: Int) {
        REFUEL(1),
        FAULT(2),
        CAR(3)
    }

    enum class ViolationType(val id: Int) {
        SPEEDING(1),
        REFUEL(2),
        WASHING(3)
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

    enum class FuelType(val  id: Int) {
        PETROL_92(1),
        PETROL_95(2),
        DIESEL(3);

        companion object {
            fun getById(id: Int): FuelType? {
                return entries.firstOrNull { it.id == id }
            }
        }
    }

    enum class Positions(val id: Int) {
        MECHANIC(positionsConf.getInt("mechanic")),
        JUNIOR_MECHANIC(positionsConf.getInt("junior_mechanic")),
        WASHER(positionsConf.getInt("washer")),
        DRIVER(positionsConf.getInt("driver"))
    }

    enum class CarStatus(val id: Int) {
        FREE(1),
        IN_USE(2),
        UNDER_REPAIR(3)
    }

    const val washHoursNormalized = 0.5
}