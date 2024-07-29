package com.fleetmate.trip.modules.trip.data.model

import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.utils.database.BaseIntIdTable
import com.fleetmate.trip.modules.trip.data.dto.TripCreateDto
import com.fleetmate.trip.modules.trip.data.dto.TripUpdateDto
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object TripModel: BaseIntIdTable() {
    val field = text("field")

    fun getOne(id: Int): ResultRow? = transaction {
        select(TripModel.id, field).where {
            TripModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(TripModel.id, field).toList()
    }

    fun create(tripCreateDto: TripCreateDto): ResultRow = transaction {
        (TripModel.insert {
            it[field] = tripCreateDto.field
        }.resultedValues ?: throw InternalServerException("Failed to create trip")).first()
    }

    fun update(id: Int, tripUpdateDto: TripUpdateDto): Boolean = transaction {
        TripModel.update({TripModel.id eq id }) { it[field] = tripUpdateDto.field } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        TripModel.deleteWhere { TripModel.id eq id } != 0
    }
}