package com.fleetmate.trip.modules.violation.data.model

import com.fleetmate.lib.data.dto.violation.ViolationTypeCreateDto
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

object ViolationTypeModel : BaseIntIdTable() {
    val name = text("name")

    fun getOne(id: Int): ResultRow? = transaction {
        select(ViolationTypeModel.id, name).where {
            ViolationTypeModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(ViolationTypeModel.id, name).toList()
    }

    fun create(violationTypeCreateDto: ViolationTypeCreateDto) {
        (ViolationTypeModel.insert {
            it[name] = violationTypeCreateDto.name
        }.resultedValues ?: throw InternalServerException("Failed to create violation type")).first()
    }

    fun update(id: Int, violationTypeUpdateDto: ViolationTypeCreateDto): Boolean = transaction {
        ViolationTypeModel.update({ ViolationTypeModel.id eq id }) { it[name] = violationTypeUpdateDto.name } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        ViolationTypeModel.deleteWhere{ ViolationTypeModel.id eq id} != 0
    }
}