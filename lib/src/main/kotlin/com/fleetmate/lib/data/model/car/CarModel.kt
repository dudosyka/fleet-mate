package com.fleetmate.lib.data.model.car


import com.fleetmate.lib.data.dto.car.CarCreateDto
import com.fleetmate.lib.data.dto.car.CarUpdateDto
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

object CarModel : BaseIntIdTable() {
    val registrationNumber = text("registration_number")
    val fuelLevel = double("fuel_level")
    val mileage = double("mileage")
    val type = reference("type", CarTypeModel)

    fun getOne(id: Int): ResultRow? = transaction {
        (CarModel innerJoin CarTypeModel).select(
            CarModel.columns + CarTypeModel.columns
        ).where {
            CarModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        (CarModel innerJoin CarTypeModel).select(
            CarModel.columns + CarTypeModel.columns
        ).toList()
    }

    fun create(carCreateDto: CarCreateDto): ResultRow = transaction {
        (CarModel.insert {
            it[registrationNumber] = carCreateDto.registrationNumber
            it[fuelLevel] = carCreateDto.fuelLevel
            it[mileage] = carCreateDto.mileage
            it[type] = carCreateDto.type
        }.resultedValues ?: throw InternalServerException("Failed to create the car")).first()
    }

    fun update(id: Int, carUpdateDto: CarUpdateDto): Boolean = transaction {
        CarModel.update({ CarModel.id eq id }) {
            if (!carUpdateDto.registrationNumber.isNullOrEmpty())
                it[registrationNumber] = carUpdateDto.registrationNumber

            if (carUpdateDto.fuelLevel != null)
                it[fuelLevel] = carUpdateDto.fuelLevel

            if (carUpdateDto.mileage != null)
                it[mileage] = carUpdateDto.mileage

            if (carUpdateDto.type != null)
                it[type] = carUpdateDto.type
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        CarModel.deleteWhere{ CarModel.id eq id} != 0
    }
}