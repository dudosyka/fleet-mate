package com.fleetmate.lib.model.automobile

import com.fleetmate.lib.dto.automobile.AutomobileTypeCreateDto
import com.fleetmate.lib.dto.automobile.AutomobileTypeUpdateDto
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
import kotlin.text.isNullOrEmpty

object AutomobileTypeModel : BaseIntIdTable() {
    val name = text("name")
    val category = text("category_of_rights")
    val speedLimit = float("speed_limit")
    val speedError = float("speed_error")

    fun getOne(id: Int): ResultRow? = transaction {
        select(AutomobileTypeModel.id, name, category, speedLimit, speedError).where {
            AutomobileTypeModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(AutomobileTypeModel.id, name, category, speedLimit, speedError).toList()
    }

    fun create(automobileTypeCreateDto: AutomobileTypeCreateDto) {
        (AutomobileTypeModel.insert {
            it[name] = automobileTypeCreateDto.name
            it[category] = automobileTypeCreateDto.category
            it[speedLimit] = automobileTypeCreateDto.speedLimit
            it[speedError] = automobileTypeCreateDto.speedError
        }.resultedValues ?: throw InternalServerException("Failed to create automobile type")).first()
    }

    fun update(id: Int, automobileTypeUpdateDto: AutomobileTypeUpdateDto): Boolean = transaction {
        AutomobileTypeModel.update({ AutomobileTypeModel.id eq id })
        {
            if (!automobileTypeUpdateDto.name.isNullOrEmpty()){
                it[name] = automobileTypeUpdateDto.name
            }
            if (!automobileTypeUpdateDto.category.isNullOrEmpty()){
                it[category] = automobileTypeUpdateDto.category
            }
            if (automobileTypeUpdateDto.speedLimit != null){
                it[speedLimit] = automobileTypeUpdateDto.speedLimit
            }
            if (automobileTypeUpdateDto.speedError != null){
                it[speedError] = automobileTypeUpdateDto.speedError
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        AutomobileTypeModel.deleteWhere{ AutomobileTypeModel.id eq id} != 0
    }
}