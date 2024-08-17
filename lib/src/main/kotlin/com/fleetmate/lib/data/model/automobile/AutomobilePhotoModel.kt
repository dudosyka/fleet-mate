package com.fleetmate.lib.data.model.automobile

import com.fleetmate.lib.data.dto.automobile.AutomobilePhotoCreateDto
import com.fleetmate.lib.data.dto.automobile.AutomobilePhotoUpdateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.model.automobile.AutomobileModel
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

object AutomobilePhotoModel : BaseIntIdTable() {
    val automobile = reference("automobile", AutomobileModel)
    val trip = reference("trip", TripModel).nullable().default(null)
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

    fun create(automobilePhotoCreateDto: AutomobilePhotoCreateDto): ResultRow = transaction{
        (AutomobilePhotoModel.insert {
            it[automobile] = automobilePhotoCreateDto.automobile
            if (automobilePhotoCreateDto.trip != null){
                it[trip] = automobilePhotoCreateDto.trip
            }
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