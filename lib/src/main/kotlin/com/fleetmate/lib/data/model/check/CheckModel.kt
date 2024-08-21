package com.fleetmate.lib.model.check

import com.fleetmate.lib.data.dto.car.CheckCreateDto
import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.dto.check.CheckUpdateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.model.division.DepartmentModel
import com.fleetmate.lib.model.post.PositionModel
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
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList

object CheckModel : BaseIntIdTable() {
    val author = reference("author", UserModel)
    val startTime = timestamp("start")
    val finishTime = timestamp("finish").nullable().default(null)
    val timeExceeded = bool("time_exceeding").nullable().default(null)
    val carId = reference("carId", CarModel)

    fun getOne(id: Int): ResultRow? = transaction {

        (CheckModel innerJoin UserModel)
            .innerJoin(DepartmentModel)
            .innerJoin(PositionModel)
            .select(
                CheckModel.id,
                UserModel.id,
                UserModel.fullName,
                UserModel.email,
                UserModel.phoneNumber,
                DepartmentModel.id,
                DepartmentModel.name,
                PositionModel.id,
                PositionModel.name,
                startTime,
                finishTime,
                timeExceeded,
                carId
            ).where(
                CheckModel.id eq id
            ).firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        (CheckModel innerJoin UserModel)
            .innerJoin(DepartmentModel)
            .innerJoin(PositionModel)
            .select(
                CheckModel.id,
                UserModel.id,
                UserModel.fullName,
                UserModel.email,
                UserModel.phoneNumber,
                DepartmentModel.id,
                DepartmentModel.name,
                PositionModel.id,
                PositionModel.name,
                startTime,
                finishTime,
                timeExceeded,
                carId
            ).toList()
    }


    fun create(checkCreateDto: CheckCreateDto): ResultRow = transaction {
        (CheckModel.insert {
            it[author] = checkCreateDto.author
            it[startTime] =
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(checkCreateDto.startTime),
                    ZoneId.systemDefault()
                ).toInstant(ZoneOffset.UTC)
            if (checkCreateDto.finishTime != null){
                it[finishTime] =
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(checkCreateDto.finishTime),
                        ZoneId.systemDefault()
                    ).toInstant(ZoneOffset.UTC)
            }
            if (checkCreateDto.timeExceeded != null){
                it[timeExceeded] = checkCreateDto.timeExceeded
            }
            it[carId] = checkCreateDto.carId
        }.resultedValues ?: throw InternalServerException("Failed to create check")).first()
    }

    fun update(id: Int, checkUpdateDto: CheckUpdateDto): Boolean = transaction {
        CheckModel.update({ CheckModel.id eq id })
        {
            if (checkUpdateDto.author != null){
                it[author] = checkUpdateDto.author
            }
            if (checkUpdateDto.startTime != null){
                it[startTime] =
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(checkUpdateDto.startTime),
                        ZoneId.systemDefault()
                    ).toInstant(ZoneOffset.UTC)
            }
            if (checkUpdateDto.finishTime != null){
                it[finishTime] =
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(checkUpdateDto.finishTime),
                        ZoneId.systemDefault()
                    ).toInstant(ZoneOffset.UTC)
            }
            if (checkUpdateDto.timeExceeded != null){
                it[timeExceeded] = checkUpdateDto.timeExceeded
            }
            if (checkUpdateDto.carId != null){
                it[carId] = checkUpdateDto.carId
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        DepartmentModel.deleteWhere{ DepartmentModel.id eq id} != 0
    }
    fun start(authorId: Int, car: Int): ResultRow = transaction {
        (CheckModel.insert {
            it[author] = authorId
            it[startTime] = LocalDateTime.now().toInstant(ZoneOffset.UTC)
            it[carId] = car
        }.resultedValues ?: throw InternalServerException("Failed to create check")).first()
    }
    fun finish(checkId: Int, exceed: Boolean): ResultRow = transaction{
        update(
            checkId,
            CheckUpdateDto(
                finishTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                timeExceeded = exceed
            )
        )
        selectAll().where{
            CheckModel.id eq checkId
        }.first()
    }

    fun getCheckForFinish(checkId: Int) = transaction{
        CheckModel.select(carId, author, startTime).where(CheckModel.id eq checkId).first()
    }
}