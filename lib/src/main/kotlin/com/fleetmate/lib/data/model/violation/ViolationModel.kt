package com.fleetmate.lib.data.model.violation

import com.fleetmate.lib.data.dto.violation.ViolationCreateDto
import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.data.model.trip.TripModel
import com.fleetmate.lib.data.model.user.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList

object ViolationModel : BaseIntIdTable() {
    val type = text("type")
    val date = timestamp("date")
    val duration = double("duration").nullable().default(null)
    val hidden = bool("hidden").default(true)
    val driver = reference("driver", UserModel)
    val trip = reference("trip", TripModel)
    val car = reference("car", CarModel)
    val comment = text("comment").nullable().default(null)

    fun getOne(id: Int): ResultRow? = transaction {
            select(ViolationModel.columns)
            .where {
                ViolationModel.id eq id
            }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(ViolationModel.id, type, date, ViolationModel.duration, hidden, driver, trip, car, comment).toList()
    }

    fun create(violationCreateDto: ViolationCreateDto) : ResultRow = transaction{
        (ViolationModel.insert {
            it[date] =
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(violationCreateDto.date),
                    ZoneId.systemDefault()
                ).toInstant(ZoneOffset.UTC)
            it[type] = violationCreateDto.type.name
            it[duration] = violationCreateDto.duration
            it[hidden] = violationCreateDto.hidden != false
            it[driver] = violationCreateDto.driver
            it[trip] = violationCreateDto.trip
            it[car] = violationCreateDto.car
            it[comment] = violationCreateDto.comment

        }.resultedValues ?: throw InternalServerException("Failed to create violation")).first()
    }
}