package com.fleetmate.lib.model.check

import com.fleetmate.lib.dto.check.CheckCreateDto
import com.fleetmate.lib.dto.check.CheckUpdateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.model.automobile.AutomobileModel
import com.fleetmate.lib.model.division.DivisionModel
import com.fleetmate.lib.model.post.PostModel
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
    val timeExceeding = bool("time_exceeding").nullable().default(null)
    val automobileId = reference("automobileId", AutomobileModel)
    val driver = reference("driver", UserModel).nullable().default(null)

    fun getOne(id: Int?): ResultRow? = transaction {
        if (id == null){
            return@transaction null
        }

        (CheckModel innerJoin UserModel)
            .innerJoin(DivisionModel)
            .innerJoin(PostModel)
            .select(
                CheckModel.id,
                UserModel.id,
                UserModel.fullName,
                UserModel.email,
                UserModel.phoneNumber,
                DivisionModel.id,
                DivisionModel.name,
                PostModel.id,
                PostModel.name,
                startTime,
                finishTime,
                timeExceeding,
                automobileId
            ).where(
                CheckModel.id eq id
            ).firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        (CheckModel innerJoin UserModel)
            .innerJoin(DivisionModel)
            .innerJoin(PostModel)
            .select(
                CheckModel.id,
                UserModel.id,
                UserModel.fullName,
                UserModel.email,
                UserModel.phoneNumber,
                DivisionModel.id,
                DivisionModel.name,
                PostModel.id,
                PostModel.name,
                startTime,
                finishTime,
                timeExceeding,
                automobileId
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
            if (checkCreateDto.timeExceeding != null){
                it[timeExceeding] = checkCreateDto.timeExceeding
            }
            it[automobileId] = checkCreateDto.automobileId
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
            if (checkUpdateDto.timeExceeding != null){
                it[timeExceeding] = checkUpdateDto.timeExceeding
            }
            if (checkUpdateDto.automobileId != null){
                it[automobileId] = checkUpdateDto.automobileId
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        DivisionModel.deleteWhere{ DivisionModel.id eq id} != 0
    }
    fun start(authorId: Int, automobile: Int): ResultRow = transaction {
        (CheckModel.insert {
            it[author] = authorId
            it[startTime] = LocalDateTime.now().toInstant(ZoneOffset.UTC)
            it[automobileId] = automobile
        }.resultedValues ?: throw InternalServerException("Failed to create check")).first()
    }
    fun finish(checkId: Int): ResultRow = transaction{
        val finishTime = LocalDateTime.now()
        var exceed = false
        val check = selectAll().where{
            CheckModel.id eq checkId
        }.first()

        if (check[startTime].epochSecond < finishTime.minusMinutes(15).toEpochSecond(ZoneOffset.UTC)){
            exceed = true
        }
        update(
            checkId,
            CheckUpdateDto(
                finishTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                timeExceeding = exceed
            )
        )
        check
    }

    fun getAutomobile(checkId: Int) = transaction{
        CheckModel.select(automobileId).where(CheckModel.id eq checkId).first()
    }
}