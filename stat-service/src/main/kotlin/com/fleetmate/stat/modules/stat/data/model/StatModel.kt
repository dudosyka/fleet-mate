package com.fleetmate.stat.modules.stat.data.model

import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.utils.database.BaseIntIdTable
import com.fleetmate.stat.modules.stat.data.dto.StatCreateDto
import com.fleetmate.stat.modules.stat.data.dto.StatUpdateDto
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object StatModel: BaseIntIdTable() {
    val field = text("field")

    fun getOne(id: Int): ResultRow? = transaction {
        select(StatModel.id, field).where {
            StatModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(StatModel.id, field).toList()
    }

    fun create(statCreateDto: StatCreateDto): ResultRow = transaction {
        (StatModel.insert {
            it[field] = statCreateDto.field
        }.resultedValues ?: throw InternalServerException("Failed to create stat")).first()
    }

    fun update(id: Int, statUpdateDto: StatUpdateDto): Boolean = transaction {
        StatModel.update({StatModel.id eq id }) { it[field] = statUpdateDto.field } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        StatModel.deleteWhere { StatModel.id eq id } != 0
    }
}