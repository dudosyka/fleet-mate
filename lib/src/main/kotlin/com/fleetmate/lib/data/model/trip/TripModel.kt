package com.fleetmate.lib.model.trip

import com.fleetmate.lib.dto.trip.TripCreateDto
import com.fleetmate.lib.dto.trip.TripUpdateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.model.check.CheckModel
import com.fleetmate.lib.model.automobile.AutomobileModel
import com.fleetmate.lib.model.user.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import kotlin.collections.first
import kotlin.collections.toList

object TripModel: BaseIntIdTable() {
    val keyAcceptance = timestamp("key acceptance")
    val status = text("status")
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
            it[keyAcceptance] = timestamp(tripCreateDto.keyAcceptance.toString())
            it[status] = tripCreateDto.status
            it[mechanicCheckBeforeTrip] = tripCreateDto.mechanicCheckBeforeTrip
            it[driverCheckBeforeTrip] = tripCreateDto.driverCheckBeforeTrip
            it[mechanicCheckAfterTrip] = tripCreateDto.mechanicCheckAfterTrip
            it[driverCheckAfterTrip] = tripCreateDto.driverCheckAfterTrip
            if (tripCreateDto.keyReturn != null){
                it[keyReturn] = timestamp(tripCreateDto.keyReturn.toString())
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
                it[keyAcceptance] = timestamp(tripUpdateDto.keyAcceptance.toString())
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
                it[keyReturn] = timestamp(tripUpdateDto.keyReturn.toString())
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
}