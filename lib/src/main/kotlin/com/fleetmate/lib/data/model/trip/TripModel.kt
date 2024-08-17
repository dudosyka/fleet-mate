package com.fleetmate.lib.model.trip

import com.fleetmate.faults.modules.faults.data.model.FaultsModel
import com.fleetmate.lib.data.dto.violation.TripViolationOutputDto
import com.fleetmate.lib.data.dto.violation.ViolationOutputDto
import com.fleetmate.lib.dto.trip.TripCreateDto
import com.fleetmate.lib.dto.trip.TripUpdateDto
import com.fleetmate.lib.exceptions.ForbiddenException
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.model.check.CheckModel
import com.fleetmate.lib.model.automobile.AutomobileModel
import com.fleetmate.lib.model.user.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import com.fleetmate.trip.modules.violation.data.model.ViolationModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.collections.first
import kotlin.collections.toList

object TripModel: BaseIntIdTable() {
    val keyAcceptance = timestamp("key acceptance")
    val status = text("status").nullable().default(null)
    val mechanicCheckBeforeTrip = reference("mechanic_check_before_trip", CheckModel).nullable().default(null)
    val driverCheckBeforeTrip = reference("driver_check_before_trip", CheckModel).nullable().default(null)
    val mechanicCheckAfterTrip = reference("mechanic_check_after_trip", CheckModel).nullable().default(null)
    val driverCheckAfterTrip = reference("driver_check_after_trip", CheckModel).nullable().default(null)
    val keyReturn = timestamp("key_return").nullable().default(null)
    val route = text("route").nullable().default(null)
    val speedInfo = array<Float>("speed_info").nullable().default(null)
    val avgSpeed = float("avg_speed").nullable().default(null)
    val driver = reference("driver", UserModel)
    val automobile = reference("automobile", AutomobileModel)
    val questionable = bool("questionable").nullable().default(null)
    val needWashing = bool("need_washing").nullable().default(null)
    val washHappen = bool("wash_happen").nullable().default(null)


    fun getOne(id: Int?): List<ResultRow?>? = transaction {
        if (id == null){
            return@transaction null
        }
        val checksInfo = TripModel.select(
            mechanicCheckBeforeTrip,
            driverCheckBeforeTrip,
            mechanicCheckAfterTrip,
            driverCheckAfterTrip,
            driver,
            automobile
        ).where(
            TripModel.id eq id
        ).firstOrNull()


        val mechanicBefore = CheckModel.getOne(checksInfo?.get(mechanicCheckAfterTrip)?.value)
        val driverBefore = CheckModel.getOne(checksInfo?.get(driverCheckBeforeTrip)?.value)
        val mechanicAfter = CheckModel.getOne(checksInfo?.get(mechanicCheckAfterTrip)?.value)
        val driverAfter = CheckModel.getOne(checksInfo?.get(driverCheckAfterTrip)?.value)

        val driverInfo : ResultRow? = UserModel.getOne(checksInfo?.get(driver)?.value)

        val automobileInfo = AutomobileModel.getOne(checksInfo?.get(automobile)?.value)

        val trip = TripModel.select(
                TripModel.id,
                keyAcceptance,
                status,
                keyReturn,
                route,
                speedInfo,
                avgSpeed,
                questionable,
                needWashing,
                washHappen
            ).where(
                TripModel.id eq id
            ).firstOrNull()

        listOf<ResultRow?>(
            mechanicBefore,
            driverBefore,
            mechanicAfter,
            driverAfter,
            driverInfo,
            automobileInfo,
            trip
        )
    }

    fun getAll(): List<ResultRow?> = transaction {
        TripModel.selectAll().toList()
    }

    fun create(tripCreateDto: TripCreateDto): ResultRow = transaction {
        (TripModel.insert {
            it[keyAcceptance] =
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(tripCreateDto.keyAcceptance),
                    ZoneId.systemDefault()
                ).toInstant(ZoneOffset.UTC)

            it[status] = tripCreateDto.status
            it[mechanicCheckBeforeTrip] = tripCreateDto.mechanicCheckBeforeTrip
            it[driverCheckBeforeTrip] = tripCreateDto.driverCheckBeforeTrip
            it[mechanicCheckAfterTrip] = tripCreateDto.mechanicCheckAfterTrip
            it[driverCheckAfterTrip] = tripCreateDto.driverCheckAfterTrip
            if (tripCreateDto.keyReturn != null){
                it[keyReturn] =
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(tripCreateDto.keyReturn),
                        ZoneId.systemDefault()
                    ).toInstant(ZoneOffset.UTC)

            }
            it[route] = tripCreateDto.route
            it[speedInfo] = tripCreateDto.speedInfo
            it[avgSpeed] = tripCreateDto.avgSpeed
            it[driver] = tripCreateDto.driver
            it[automobile] = tripCreateDto.automobile
            it[questionable] = tripCreateDto.questionable
            it[needWashing] = tripCreateDto.needWashing
            it[washHappen] = tripCreateDto.washHappen
        }.resultedValues ?: throw InternalServerException("Failed to create trip")).first()
    }

    fun update(id: Int, tripUpdateDto: TripUpdateDto): Boolean = transaction {
        TripModel.update({TripModel.id eq id }) {
            if (tripUpdateDto.keyAcceptance != null){
                it[keyAcceptance] =
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(tripUpdateDto.keyAcceptance),
                        ZoneId.systemDefault()
                    ).toInstant(ZoneOffset.UTC)
            }
            if (tripUpdateDto.status != null){
                it[status] = tripUpdateDto.status
            }
            if (tripUpdateDto.mechanicCheckBeforeTrip != null){
                it[mechanicCheckBeforeTrip] = tripUpdateDto.mechanicCheckBeforeTrip
            }
            if (tripUpdateDto.driverCheckBeforeTrip != null) {
                it[driverCheckBeforeTrip] = tripUpdateDto.driverCheckBeforeTrip
            }
            if (tripUpdateDto.mechanicCheckAfterTrip != null){
                it[mechanicCheckAfterTrip] = tripUpdateDto.mechanicCheckAfterTrip
            }
            if (tripUpdateDto.driverCheckAfterTrip != null){
                it[driverCheckAfterTrip] = tripUpdateDto.driverCheckAfterTrip
            }
            if (tripUpdateDto.keyReturn != null){
                it[keyReturn] =
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(tripUpdateDto.keyReturn),
                        ZoneId.systemDefault()
                    ).toInstant(ZoneOffset.UTC)
            }
            if (tripUpdateDto.route != null){
                it[route] = tripUpdateDto.route
            }
            if (tripUpdateDto.speedInfo != null){
                it[speedInfo] = tripUpdateDto.speedInfo
            }
            if (tripUpdateDto.avgSpeed != null){
                it[avgSpeed] = tripUpdateDto.avgSpeed
            }
            if (tripUpdateDto.driver != null){
                it[driver] = tripUpdateDto.driver
            }
            if (tripUpdateDto.automobile != null){
                it[automobile] = tripUpdateDto.automobile
            }
            if (tripUpdateDto.questionable != null){
                it[questionable] = tripUpdateDto.questionable
            }
            if (tripUpdateDto.needWashing != null){
                it[needWashing] = tripUpdateDto.needWashing
            }
            if (tripUpdateDto.washHappen != null){
                it[washHappen] = tripUpdateDto.washHappen
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        TripModel.deleteWhere { TripModel.id eq id } != 0
    }

    fun getAllDriverTrip(driverId: Int, day: Int) = transaction {
        val currentDay = LocalDateTime.now().dayOfMonth

        if (currentDay >= day) {
            val time = LocalDateTime.now().withDayOfMonth(day)
            val trips = innerJoin(AutomobileModel)
                .select(
                    TripModel.id,
                    keyAcceptance,
                    keyReturn,
                    route,
                    avgSpeed,
                    AutomobileModel.id,
                    AutomobileModel.mileage,
                    AutomobileModel.stateNumber,
                    AutomobileModel.fuelLevel
                ).where(
                    driver eq driverId
                ).andWhere {
                    keyAcceptance greater time.toInstant(ZoneOffset.UTC)
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
    }
    fun initTrip(driverId: Int, automobileId: Int): ResultRow = transaction{
        val time = LocalDateTime.now().minusDays(1).toInstant(ZoneOffset.UTC)
        val check = CheckModel.select(
            CheckModel.driver,
            CheckModel.finishTime
        ).where{
            CheckModel.driver eq driverId
        }.andWhere {
            CheckModel.finishTime greater time
        }.firstOrNull()

        val faults = FaultsModel.select(
            FaultsModel.automobile,
            FaultsModel.critical
        ).where{
            FaultsModel.automobile eq automobileId
        }.adjustWhere {
            FaultsModel.critical eq true
        }.firstOrNull()

        if (check == null || faults != null){
            throw ForbiddenException()
        }
        else{
            return@transaction (
                    TripModel.insert {
                        it[keyAcceptance] = time
                        it[driver] = driverId
                        it[automobile] = automobileId
                    }.resultedValues ?: throw InternalServerException("Failed to create trip")
            ).first()
        }
    }
    fun getActiveTrip(automobileId: Int): ResultRow? = transaction{
        TripModel.select(
            TripModel.id,
            washHappen,
            driver
        ).where(
            automobile eq automobileId
        ).andWhere {
            keyReturn eq null
        }.firstOrNull()
    }
}