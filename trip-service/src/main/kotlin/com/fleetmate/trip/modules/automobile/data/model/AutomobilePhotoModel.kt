package com.fleetmate.trip.modules.automobile.data.model

import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.model.automobile.AutomobileModel
import com.fleetmate.lib.model.trip.TripModel
import com.fleetmate.lib.model.user.UserModel
import com.fleetmate.lib.model.photo.PhotoModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import com.fleetmate.trip.modules.automobile.data.dto.AutomobilePhotoCreateDto
import com.fleetmate.trip.modules.automobile.data.dto.AutomobilePhotoUpdateDto
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList

object AutomobilePhotoModel : BaseIntIdTable() {
    val automobile = reference("automobile", AutomobileModel)
    val trip = reference("trip", TripModel)
    val driver = reference("driver", UserModel)
    val photo = reference("photo", PhotoModel)

    fun getOne(id: Int): ResultRow? = transaction {
        select(AutomobilePhotoModel.id, automobile, trip, driver, createdAt, photo).where {
            AutomobilePhotoModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(AutomobilePhotoModel.id, automobile, trip, driver, createdAt, photo).toList()
    }

    fun create(automobilePhotoCreateDto: AutomobilePhotoCreateDto) {
        (AutomobilePhotoModel.insert {
            it[automobile] = automobilePhotoCreateDto.automobile
            it[trip] = automobilePhotoCreateDto.trip
            it[driver] = automobilePhotoCreateDto.driver
            it[photo] = automobilePhotoCreateDto.photo
        }.resultedValues ?: throw InternalServerException("Failed to create automobile photo")).first()
    }

    fun update(id: Int, automobilePhotoUpdateDto: AutomobilePhotoUpdateDto): Boolean = transaction {
        AutomobilePhotoModel.update({ AutomobilePhotoModel.id eq id })
        {
            if (automobilePhotoUpdateDto.automobile != null){
                it[automobile] = automobilePhotoUpdateDto.automobile
            }
            if (automobilePhotoUpdateDto.trip != null){
                it[trip] = automobilePhotoUpdateDto.trip
            }
            if (automobilePhotoUpdateDto.driver != null){
                it[driver] = automobilePhotoUpdateDto.driver
            }
            if (automobilePhotoUpdateDto.photo != null){
                it[photo] = automobilePhotoUpdateDto.photo
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        AutomobilePhotoModel.deleteWhere{ AutomobilePhotoModel.id eq id} != 0
    }
}