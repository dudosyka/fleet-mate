package com.fleetmate.lib.model.automobile

import com.fleetmate.lib.dto.automobile.AutomobileCreateDto
import com.fleetmate.lib.dto.automobile.AutomobileUpdateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.javatime.timestampParam
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.Date
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList
import kotlin.text.isNullOrEmpty

object AutomobileModel : BaseIntIdTable() {
    val stateNumber = text("state_number")
    val fuelLevel = float("fuel_level")
    val mileage = float("mileage")
    val additionDate = timestamp("addition_date")
    val type = reference("type", AutomobileTypeModel)

    fun getOne(id: Int?): ResultRow? = transaction {
        if (id == null){
            return@transaction null
        }
        (AutomobileModel innerJoin AutomobileTypeModel).select(
            AutomobileModel.id,
            stateNumber,
            fuelLevel,
            mileage,
            additionDate,
            AutomobileTypeModel.id,
            AutomobileTypeModel.name,
            AutomobileTypeModel.speedLimit,
            AutomobileTypeModel.speedError,
            AutomobileTypeModel.category
        ).where {
            AutomobileModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        (AutomobileModel innerJoin AutomobileTypeModel).select(
            AutomobileModel.id,
            stateNumber,
            fuelLevel,
            mileage,
            additionDate,
            AutomobileTypeModel.id,
            AutomobileTypeModel.name,
            AutomobileTypeModel.speedLimit,
            AutomobileTypeModel.speedError,
            AutomobileTypeModel.category
        ).toList()
    }

    fun create(automobileCreateDto: AutomobileCreateDto): ResultRow = transaction {
        (AutomobileModel.insert {
            it[stateNumber] = automobileCreateDto.stateNumber
            it[fuelLevel] = automobileCreateDto.fuelLevel
            it[mileage] = automobileCreateDto.mileage
            it[additionDate] = timestampParam(Date().toInstant())
            it[type] = automobileCreateDto.type
        }.resultedValues ?: throw InternalServerException("Failed to create automobile")).first()
    }

    fun update(id: Int, automobileUpdateDto: AutomobileUpdateDto): Boolean = transaction {
        AutomobileModel.update({ AutomobileModel.id eq id })
        {
            if (!automobileUpdateDto.stateNumber.isNullOrEmpty()){
                it[stateNumber] = automobileUpdateDto.stateNumber
            }
            if (automobileUpdateDto.fuelLevel != null){
                it[fuelLevel] = automobileUpdateDto.fuelLevel
            }
            if (automobileUpdateDto.mileage != null){
                it[mileage] = automobileUpdateDto.mileage
            }
            if (automobileUpdateDto.additionalDate != null){
                it[additionDate] = timestampParam(Date().toInstant())
            }
            if (automobileUpdateDto.type != null){
                it[type] = automobileUpdateDto.type
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        AutomobileModel.deleteWhere{ AutomobileModel.id eq id} != 0
    }
}