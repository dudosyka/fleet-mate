package com.fleetmate.lib.model.division

import com.fleetmate.lib.dto.division.DepartmentCreateDto
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

object DepartmentModel : BaseIntIdTable() {
    val name = text("name")

    fun getOne(id: Int): ResultRow? = transaction {
        select(DepartmentModel.id, name).where {
            DepartmentModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(DepartmentModel.id, name).toList()
    }

    fun create(departmentCreateDto: DepartmentCreateDto): ResultRow = transaction{
        (DepartmentModel.insert {
            it[name] = departmentCreateDto.name
        }.resultedValues ?: throw InternalServerException("Failed to create division")).first()
    }

    fun update(id: Int, divisionUpdateDto: DepartmentCreateDto): Boolean = transaction {
        DepartmentModel.update({ DepartmentModel.id eq id })
        {
            it[name] = divisionUpdateDto.name
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        DepartmentModel.deleteWhere{ DepartmentModel.id eq id} != 0
    }
}