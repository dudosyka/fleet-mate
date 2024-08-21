package com.fleetmate.lib.data.model.faults

import com.fleetmate.lib.data.dto.faults.FaultsCreateDto
import com.fleetmate.lib.data.dto.faults.FaultsUpdateDto
import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.data.dto.auth.AuthorizedUser
import com.fleetmate.lib.data.model.car.CarPartModel
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.data.model.trip.TripModel
import com.fleetmate.lib.data.model.user.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object FaultsModel: BaseIntIdTable() {
    val status = text("status")
    val trip = reference("trip", TripModel).nullable().default(null)
    val user = reference("user", UserModel)
    val car = reference("car", CarModel)
    val carPart = reference("car_part", CarPartModel)
    val comment = text("comment").nullable().default(null)
    val critical = bool("critical")

    fun getOne(id: Int): ResultRow? = transaction {
            innerJoin(CarModel)
            .select(
                FaultsModel.columns + CarModel.columns,
            )
            .where {
                FaultsModel.id eq id
            }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        selectAll().toList()
    }

    fun create(authorizedUser: AuthorizedUser, faultsCreateDto: FaultsCreateDto): ResultRow = transaction {
        (FaultsModel.insert {
            it[status] = faultsCreateDto.tripStatus.name
            it[trip] = faultsCreateDto.trip
            it[user] = authorizedUser.id
            it[car] = faultsCreateDto.car
            it[comment] = faultsCreateDto.comment
            it[critical] = true
        }.resultedValues ?: throw InternalServerException("Failed to create fault")).first()
    }

    fun update(id: Int, faultsUpdateDto: FaultsUpdateDto): Boolean = transaction {
        FaultsModel.update({ FaultsModel.id eq id }) {

            if (faultsUpdateDto.tripStatus != null)
                it[status] = faultsUpdateDto.tripStatus.name

            if (faultsUpdateDto.trip != null)
                it[trip] = faultsUpdateDto.trip

            if (faultsUpdateDto.car != null)
                it[car] = faultsUpdateDto.car

            if (faultsUpdateDto.comment != null)
                it[comment] = faultsUpdateDto.comment

            if (faultsUpdateDto.critical != null)
                it[critical] = faultsUpdateDto.critical

        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        FaultsModel.deleteWhere { FaultsModel.id eq id } != 0
    }

    fun getAllCriticalByCar(carId: Int): List<ResultRow> = transaction {
            select(
                FaultsModel.columns
            )
            .where {
                car eq carId
            }.andWhere {
                critical eq true
            }.toList()
    }

    fun markAllAsNotCriticalForCar(carId: Int) = transaction {
        update({ car eq carId }) {
            it[critical] = false
        }
    }

    fun getAllByTrip(tripId: Int): List<ResultRow?> = transaction {
        select(
            FaultsModel.id
        ).where{
            trip eq tripId
        }.toList()
    }

    fun getByCarPart(carId: Int, carPart: Int) = transaction {
        selectAll().where {
            (car eq carId) and (FaultsModel.carPart eq carPart)
        }.toList()
    }
}