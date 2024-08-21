package com.fleetmate.lib.data.model.position

import com.fleetmate.lib.data.dto.position.PositionCreateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList

object PositionModel : BaseIntIdTable() {
    val name = text("name")

    fun getOne(id: Int): ResultRow? = transaction {
        select(PositionModel.id, name).where {
            PositionModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(PositionModel.id, name).toList()
    }

    fun create(positionCreateDto: PositionCreateDto): ResultRow = transaction {
        (PositionModel.insert {
            it[name] = positionCreateDto.name
        }.resultedValues ?: throw InternalServerException("Failed to create post")).first()
    }

    fun update(id: Int, postUpdateDto: PositionCreateDto): Boolean = transaction {
        PositionModel.update({ PositionModel.id eq id }) { it[name] = postUpdateDto.name } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        PositionModel.deleteWhere{ PositionModel.id eq id} != 0
    }
}