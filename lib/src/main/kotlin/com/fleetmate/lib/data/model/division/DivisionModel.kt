package com.fleetmate.lib.model.division

import com.fleetmate.lib.dto.division.DivisionCreateDto
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

object DivisionModel : BaseIntIdTable() {
    val name = text("name")

    fun getOne(id: Int): ResultRow? = transaction {
        select(DivisionModel.id, name).where {
            DivisionModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(DivisionModel.id, name).toList()
    }

    fun create(divisionCreateDto: DivisionCreateDto): ResultRow = transaction{
        (DivisionModel.insert {
            it[name] = divisionCreateDto.name
        }.resultedValues ?: throw InternalServerException("Failed to create division")).first()
    }

    fun update(id: Int, divisionUpdateDto: DivisionCreateDto): Boolean = transaction {
        DivisionModel.update({ DivisionModel.id eq id })
        {
            it[name] = divisionUpdateDto.name
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        DivisionModel.deleteWhere{ DivisionModel.id eq id} != 0
    }
}