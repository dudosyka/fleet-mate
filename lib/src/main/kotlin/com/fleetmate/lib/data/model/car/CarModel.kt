package com.fleetmate.lib.data.model.car


import com.fleetmate.lib.data.dto.car.CarCreateDto
import com.fleetmate.lib.data.dto.car.CarUpdateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList
import kotlin.text.isNullOrEmpty

object CarModel : BaseIntIdTable() {
    val registrationNumber = text("registration_number")
    val fuelLevel = float("fuel_level")
    val mileage = float("mileage")
    val dateAdded = timestamp("date_added")
    val type = reference("type", CarTypeModel)

    fun getOne(id: Int): ResultRow? = transaction {
        (CarModel innerJoin CarTypeModel).select(
            CarModel.id,
            registrationNumber,
            fuelLevel,
            mileage,
            dateAdded,
            CarTypeModel.id,
            CarTypeModel.name,
            CarTypeModel.speedLimit,
            CarTypeModel.speedError,
            CarTypeModel.category,
            CarTypeModel.avgFuelConsumption
        ).where {
            CarModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        (CarModel innerJoin CarTypeModel).select(
            CarModel.id,
            registrationNumber,
            fuelLevel,
            mileage,
            dateAdded,
            CarTypeModel.id,
            CarTypeModel.name,
            CarTypeModel.speedLimit,
            CarTypeModel.speedError,
            CarTypeModel.category,
            CarTypeModel.avgFuelConsumption
        ).toList()
    }

    fun create(carCreateDto: CarCreateDto): ResultRow = transaction {
        (CarModel.insert {
            it[registrationNumber] = carCreateDto.registrationNumber
            it[fuelLevel] = carCreateDto.fuelLevel
            it[mileage] = carCreateDto.mileage
            it[dateAdded] =
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(carCreateDto.additionalDate),
                    ZoneId.systemDefault()
                ).toInstant(ZoneOffset.UTC)
            it[type] = carCreateDto.type
        }.resultedValues ?: throw InternalServerException("Failed to create the car")).first()
    }

    fun update(id: Int, carUpdateDto: CarUpdateDto): Boolean = transaction {
        CarModel.update({ CarModel.id eq id })
        {
            if (!carUpdateDto.registrationNumber.isNullOrEmpty()){
                it[registrationNumber] = carUpdateDto.registrationNumber
            }
            if (carUpdateDto.fuelLevel != null){
                it[fuelLevel] = carUpdateDto.fuelLevel
            }
            if (carUpdateDto.mileage != null){
                it[mileage] = carUpdateDto.mileage
            }
            if (carUpdateDto.dateAdded != null){
                it[dateAdded] =
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(carUpdateDto.dateAdded),
                        ZoneId.systemDefault()
                    ).toInstant(ZoneOffset.UTC)
            }
            if (carUpdateDto.type != null){
                it[type] = carUpdateDto.type
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        CarModel.deleteWhere{ CarModel.id eq id} != 0
    }
}