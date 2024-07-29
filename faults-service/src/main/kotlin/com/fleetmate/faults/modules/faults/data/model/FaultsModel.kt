package com.fleetmate.faults.modules.faults.data.model

import com.fleetmate.faults.modules.faults.data.dto.FaultsCreateDto
import com.fleetmate.faults.modules.faults.data.dto.FaultsUpdateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object FaultsModel: BaseIntIdTable() {
    val field = text("field")

    fun getOne(id: Int): ResultRow? = transaction {
        select(FaultsModel.id, field).where {
            FaultsModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(FaultsModel.id, field).toList()
    }

    fun create(faultsCreateDto: FaultsCreateDto): ResultRow = transaction {
        (FaultsModel.insert {
            it[field] = faultsCreateDto.field
        }.resultedValues ?: throw InternalServerException("Failed to create fault")).first()
    }

    fun update(id: Int, faultsUpdateDto: FaultsUpdateDto): Boolean = transaction {
        FaultsModel.update({FaultsModel.id eq id }) { it[field] = faultsUpdateDto.field } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        FaultsModel.deleteWhere { FaultsModel.id eq id } != 0
    }
}