package com.fleetmate.lib.data.model.car

import com.fleetmate.lib.data.dto.car.CarPhotoCreateDto
import com.fleetmate.lib.data.dto.car.CarPhotoUpdateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.model.trip.TripModel
import com.fleetmate.lib.model.user.UserModel
import com.fleetmate.lib.model.photo.PhotoModel
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

object CarPhotoModel : BaseIntIdTable() {
    val car = reference("car", CarModel)
    val trip = reference("trip", TripModel).nullable().default(null)
    val driver = reference("driver", UserModel)
    val photo = reference("photo", PhotoModel)

    fun getOne(id: Int): ResultRow? = transaction {
        select(CarPhotoModel.id, car, trip, driver, createdAt, photo).where {
            CarPhotoModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(CarPhotoModel.id, car, trip, driver, createdAt, photo).toList()
    }

    fun create(carPhotoCreateDto: CarPhotoCreateDto): ResultRow = transaction{
        (CarPhotoModel.insert {
            it[car] = carPhotoCreateDto.car
            if (carPhotoCreateDto.trip != null){
                it[trip] = carPhotoCreateDto.trip
            }
            it[driver] = carPhotoCreateDto.driver
            it[photo] = carPhotoCreateDto.photo
        }.resultedValues ?: throw InternalServerException("Failed to create car photo")).first()
    }

    fun update(id: Int, carPhotoUpdateDto: CarPhotoUpdateDto): Boolean = transaction {
        CarPhotoModel.update({ CarPhotoModel.id eq id })
        {
            if (carPhotoUpdateDto.car != null){
                it[car] = carPhotoUpdateDto.car
            }
            if (carPhotoUpdateDto.trip != null){
                it[trip] = carPhotoUpdateDto.trip
            }
            if (carPhotoUpdateDto.driver != null){
                it[driver] = carPhotoUpdateDto.driver
            }
            if (carPhotoUpdateDto.photo != null){
                it[photo] = carPhotoUpdateDto.photo
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        CarPhotoModel.deleteWhere{ CarPhotoModel.id eq id} != 0
    }
}