package com.fleetmate.trip.modules.violation.data.model

import com.fleetmate.lib.data.dto.violation.ViolationCreateDto
import com.fleetmate.lib.data.dto.violation.ViolationUpdateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.model.automobile.AutomobileModel
import com.fleetmate.lib.model.trip.TripModel
import com.fleetmate.lib.model.user.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList

object ViolationModel : BaseIntIdTable() {
    val type = reference("type", ViolationTypeModel)
    val date = timestamp("date")
    val duration = float("duration").nullable().default(null)
    val hidden = bool("hidden").default(true)
    val driver = reference("driver", UserModel)
    val trip = reference("trip", TripModel)
    val automobile = reference("automobile", AutomobileModel)
    val comment = text("comment").nullable().default(null)

    fun getOne(id: Int): ResultRow? = transaction {
        (ViolationModel innerJoin ViolationTypeModel)
            .select(
                ViolationTypeModel.id,
                ViolationTypeModel.name,
                ViolationModel.id,
                date,
                ViolationModel.duration,
                hidden,
                driver,
                trip,
                automobile,
                comment
            )
            .where {
                ViolationModel.id eq id
            }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(ViolationModel.id, type, date, ViolationModel.duration, hidden, driver, trip, automobile, comment).toList()
    }

    fun create(violationCreateDto: ViolationCreateDto) : ResultRow = transaction{
        (ViolationModel.insert {
            it[date] =
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(violationCreateDto.date),
                    ZoneId.systemDefault()
                ).toInstant(ZoneOffset.UTC)
            it[type] = violationCreateDto.type
            it[duration] = violationCreateDto.duration
            it[hidden] = violationCreateDto.hidden != false
            it[driver] = violationCreateDto.driver
            it[trip] = violationCreateDto.trip
            it[automobile] = violationCreateDto.automobile
            it[comment] = violationCreateDto.comment

        }.resultedValues ?: throw InternalServerException("Failed to create violation")).first()
    }

    fun update(id: Int, violationUpdateDto: ViolationUpdateDto): Boolean = transaction {
        ViolationModel.update({ ViolationModel.id eq id })
        {
            if (violationUpdateDto.date != null){
                it[date] =
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(violationUpdateDto.date),
                        ZoneId.systemDefault()
                    ).toInstant(ZoneOffset.UTC)
            }
            if (violationUpdateDto.type != null){
                it[type] = violationUpdateDto.type
            }
            if (violationUpdateDto.duration != null){
                it[duration] = violationUpdateDto.duration
            }
            if (violationUpdateDto.hidden != null){
                it[hidden] = violationUpdateDto.hidden
            }
            if (violationUpdateDto.driver != null){
                it[driver] = violationUpdateDto.driver
            }
            if (violationUpdateDto.trip != null){
                it[trip] = violationUpdateDto.trip
            }
            if (violationUpdateDto.automobile != null){
                it[automobile] = violationUpdateDto.automobile
            }
            if (violationUpdateDto.comment != null){
                it[comment] = violationUpdateDto.comment
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        ViolationModel.deleteWhere{ ViolationModel.id eq id} != 0
    }
}