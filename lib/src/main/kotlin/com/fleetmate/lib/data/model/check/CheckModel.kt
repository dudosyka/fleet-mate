package com.fleetmate.lib.model.check

import com.fleetmate.lib.dto.check.CheckCreateDto
import com.fleetmate.lib.dto.check.CheckUpdateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.model.division.DivisionModel
import com.fleetmate.lib.model.post.PostModel
import com.fleetmate.lib.model.user.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList

object CheckModel : BaseIntIdTable() {
    val author = reference("author", UserModel)
    val startTime = timestamp("start")
    val finishTime = timestamp("finish").nullable().default(null)
    val timeExceeding = bool("time_exceeding").nullable().default(null)

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
                timeExceeding
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
                timeExceeding
            ).toList()
    }


    fun create(checkCreateDto: CheckCreateDto): ResultRow = transaction {
        (CheckModel.insert {
            it[author] = checkCreateDto.author
            it[startTime] = timestamp(checkCreateDto.startTime.toString())
            if (checkCreateDto.finishTime != null){
                it[finishTime] = timestamp(checkCreateDto.finishTime.toString())
            }
            if (checkCreateDto.timeExceeding != null){
                it[timeExceeding] = checkCreateDto.timeExceeding
            }
        }.resultedValues ?: throw InternalServerException("Failed to create check")).first()
    }

    fun update(id: Int, checkUpdateDto: CheckUpdateDto): Boolean = transaction {
        CheckModel.update({ CheckModel.id eq id })
        {
            if (checkUpdateDto.author != null){
                it[author] = checkUpdateDto.author
            }
            if (checkUpdateDto.startTime != null){
                it[startTime] = timestamp(checkUpdateDto.startTime.toString())
            }
            if (checkUpdateDto.finishTime != null){
                it[finishTime] = timestamp(checkUpdateDto.finishTime.toString())
            }
            if (checkUpdateDto.timeExceeding != null){
                it[timeExceeding] = checkUpdateDto.timeExceeding
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        DivisionModel.deleteWhere{ DivisionModel.id eq id} != 0
    }
}