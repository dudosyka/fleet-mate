package com.fleetmate.lib.data.model.trip

import com.fleetmate.lib.conf.AppConf
import com.fleetmate.lib.data.dto.violation.TripViolationOutputDto
import com.fleetmate.lib.data.dto.violation.ViolationOutputDto
import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.data.dto.trip.TripUpdateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.data.model.check.CheckModel
import com.fleetmate.lib.data.model.user.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import com.fleetmate.lib.data.model.violation.ViolationModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import kotlin.collections.first
import kotlin.collections.toList

object TripModel: BaseIntIdTable() {
    val keyAcceptance = long("key_acceptance")
    val status = text("status")
    val mechanicCheckBeforeTrip = reference("mechanic_check_before_trip", CheckModel).nullable().default(null)
    val driverCheckBeforeTrip = reference("driver_check_before_trip", CheckModel).nullable().default(null)
    val mechanicCheckAfterTrip = reference("mechanic_check_after_trip", CheckModel).nullable().default(null)
    val driverCheckAfterTrip = reference("driver_check_after_trip", CheckModel).nullable().default(null)
    val keyReturn = long("key_return").nullable().default(null)
    val route = text("route").nullable().default(null)
    val speedInfo = array<Double>("speed_info").default(listOf())
    val avgSpeed = double("avg_speed").nullable().default(null)
    val driver = reference("driver", UserModel)
    val car = reference("car", CarModel)
    val questionable = bool("questionable").nullable().default(null)
    val needWashing = bool("need_washing").nullable().default(null)
    val washHappen = bool("wash_happen").nullable().default(null)

    data class TripResultRow(
        val tripData: ResultRow,
        val mechanicCheckBefore: ResultRow?,
        val mechanicCheckAfter: ResultRow?,
        val driverCheckBefore: ResultRow?,
        val driverCheckAfter: ResultRow?,
    )

    fun getOne(id: Int): TripResultRow? = transaction {
        val checksPool = TripModel.select(
            mechanicCheckBeforeTrip,
            driverCheckBeforeTrip,
            mechanicCheckAfterTrip,
            driverCheckAfterTrip,
            driver,
            car
        ).where(
            TripModel.id eq id
        ).firstOrNull() ?: return@transaction null

        TripResultRow(
            tripData = TripModel
                .leftJoin(CarModel)
                .join(UserModel, JoinType.LEFT, driver, UserModel.id)
                .select(TripModel.columns.plus(CarModel.columns).plus(UserModel.columns)).where(
                    TripModel.id eq id
                ).firstOrNull() ?: return@transaction null,

            mechanicCheckBefore = if (checksPool[mechanicCheckBeforeTrip] != null){
                CheckModel.getOne(checksPool[mechanicCheckBeforeTrip]!!.value)
            } else null,

            mechanicCheckAfter = if (checksPool[mechanicCheckAfterTrip] != null){
                CheckModel.getOne(checksPool[mechanicCheckAfterTrip]!!.value)
            } else null,

            driverCheckBefore = if (checksPool[driverCheckBeforeTrip] != null){
                CheckModel.getOne(checksPool[driverCheckBeforeTrip]!!.value)
            } else null,

            driverCheckAfter = if (checksPool[driverCheckAfterTrip] != null){
                CheckModel.getOne(checksPool[driverCheckAfterTrip]!!.value)
            } else null,
        )
    }

    fun getAll(): List<ResultRow> = transaction {
        TripModel.selectAll().toList()
    }

    fun create(driverId: Int, carId: Int): ResultRow = transaction {
        (TripModel.insert {
            it[keyAcceptance] = LocalDateTime.now().toEpochSecond(AppConf.defaultZoneOffset)
            it[driver] = driverId
            it[car] = carId
        }.resultedValues ?: throw InternalServerException("Failed to create trip")).first()
    }

    fun update(id: Int, tripUpdateDto: TripUpdateDto): Boolean = transaction {
        TripModel.update({ TripModel.id eq id }) {
            if (tripUpdateDto.keyAcceptance != null)
                it[keyAcceptance] = tripUpdateDto.keyAcceptance

            if (tripUpdateDto.status != null)
                it[status] = tripUpdateDto.status.name

            if (tripUpdateDto.mechanicCheckBeforeTrip != null)
                it[mechanicCheckBeforeTrip] = tripUpdateDto.mechanicCheckBeforeTrip

            if (tripUpdateDto.driverCheckBeforeTrip != null)
                it[driverCheckBeforeTrip] = tripUpdateDto.driverCheckBeforeTrip

            if (tripUpdateDto.mechanicCheckAfterTrip != null)
                it[mechanicCheckAfterTrip] = tripUpdateDto.mechanicCheckAfterTrip

            if (tripUpdateDto.driverCheckAfterTrip != null)
                it[driverCheckAfterTrip] = tripUpdateDto.driverCheckAfterTrip

            if (tripUpdateDto.keyReturn != null)
                it[keyAcceptance] = tripUpdateDto.keyReturn

            if (tripUpdateDto.route != null)
                it[route] = tripUpdateDto.route

            if (tripUpdateDto.speedInfo != null)
                it[speedInfo] = tripUpdateDto.speedInfo

            if (tripUpdateDto.avgSpeed != null)
                it[avgSpeed] = tripUpdateDto.avgSpeed

            if (tripUpdateDto.driver != null)
                it[driver] = tripUpdateDto.driver

            if (tripUpdateDto.car != null)
                it[car] = tripUpdateDto.car

            if (tripUpdateDto.questionable != null)
                it[questionable] = tripUpdateDto.questionable

            if (tripUpdateDto.needWashing != null)
                it[needWashing] = tripUpdateDto.needWashing

            if (tripUpdateDto.washHappen != null)
                it[washHappen] = tripUpdateDto.washHappen

        } != 0
    }

    fun getAllDriverTrip(driverId: Int) = transaction {
        val time = LocalDateTime.now().toEpochSecond(AppConf.defaultZoneOffset)
        val trips = innerJoin(CarModel)
            .select(
                TripModel.id,
                keyAcceptance,
                keyReturn,
                route,
                avgSpeed,
                CarModel.id,
                CarModel.mileage,
                CarModel.registrationNumber,
                CarModel.fuelLevel
            ).where(
                driver eq driverId
            ).andWhere {
                keyAcceptance greater time
            }.toList()

        val tripsId = mutableListOf<Int>()
        trips.forEach {
            tripsId.add(it[TripModel.id].value)
        }

        val violations = ViolationModel
            .getAll()
            .filter {
                tripsId.contains(it[ViolationModel.trip].value)
            }
            .map {
                ViolationOutputDto(it)
            }

        val list = mutableListOf<TripViolationOutputDto>()
        trips.forEach { trip ->
            list.add(
                TripViolationOutputDto(
                    trip,
                    violations.filter { violation ->
                        violation.trip == trip[TripModel.id].value
                    }
                )
            )
        }

        return@transaction list
    }

    fun getActiveTrip(carId: Int): ResultRow? = transaction{
        TripModel.select(
            TripModel.id,
            washHappen,
            driver,
            car,
            mechanicCheckBeforeTrip,
            mechanicCheckAfterTrip,
            driverCheckBeforeTrip,
            driverCheckAfterTrip
        ).where(
            car eq carId
        ).andWhere {
            keyReturn eq null
        }.firstOrNull()
    }
}