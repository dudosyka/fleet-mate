package com.fleetmate.lib.data.model.car

import com.fleetmate.lib.data.dto.car.type.CarTypeCreateDto
import com.fleetmate.lib.data.dto.car.type.CarTypeUpdateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList
import kotlin.text.isNullOrEmpty

object CarTypeModel : BaseIntIdTable() {
    val name = text("name")
    val category = text("category_of_rights")
    val speedLimit = double("speed_limit")
    val speedError = double("speed_error")
    val avgFuelConsumption = double("avg_fuel_consumption")

    fun getOne(id: Int): ResultRow? = transaction {
        selectAll().where {
            CarTypeModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        selectAll().toList()
    }

    fun create(carTypeCreateDto: CarTypeCreateDto) {
        (CarTypeModel.insert {
            it[name] = carTypeCreateDto.name
            it[category] = carTypeCreateDto.category.name
            it[speedLimit] = carTypeCreateDto.speedLimit
            it[speedError] = carTypeCreateDto.speedError
            it[avgFuelConsumption] = carTypeCreateDto.avgFuelConsumption

        }.resultedValues ?: throw InternalServerException("Failed to create automobile type")).first()
    }

    fun update(id: Int, carTypeUpdateDto: CarTypeUpdateDto): Boolean = transaction {
        CarTypeModel.update({ CarTypeModel.id eq id })
        {
            if (!carTypeUpdateDto.name.isNullOrEmpty()){
                it[name] = carTypeUpdateDto.name
            }
            if (carTypeUpdateDto.category != null){
                it[category] = carTypeUpdateDto.category.name
            }
            if (carTypeUpdateDto.speedLimit != null){
                it[speedLimit] = carTypeUpdateDto.speedLimit
            }
            if (carTypeUpdateDto.speedError != null){
                it[speedError] = carTypeUpdateDto.speedError
            }
            if (carTypeUpdateDto.avgFuelConsumption != null){
                it[avgFuelConsumption] = carTypeUpdateDto.avgFuelConsumption
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        CarTypeModel.deleteWhere{ CarTypeModel.id eq id} != 0
    }
}