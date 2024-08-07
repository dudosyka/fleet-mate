package com.fleetmate.trip.modules.refuel.data.model

import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.model.automobile.AutomobileModel
import com.fleetmate.lib.model.photo.PhotoModel
import com.fleetmate.lib.model.trip.TripModel
import com.fleetmate.lib.model.user.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import com.fleetmate.trip.modules.refuel.data.dto.RefuelCreateDto
import com.fleetmate.trip.modules.refuel.data.dto.RefuelUpdateDto
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList


object RefuelModel : BaseIntIdTable() {
    val date = timestamp("date")
    val volume = float("volume")
    val automobile = reference("automobile", AutomobileModel)
    val trip = reference("trip", TripModel)
    val driver = reference("driver", UserModel)
    val billPhoto = reference("bill photo", PhotoModel)

    fun getOne(id: Int): ResultRow? = transaction {
        select(RefuelModel.id, date, volume, automobile, trip, driver, billPhoto).where {
            RefuelModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(RefuelModel.id, date, volume, automobile, trip, driver, billPhoto).toList()
    }

    fun create(refuelCreateDto: RefuelCreateDto): ResultRow = transaction {
        (RefuelModel.insert {
            it[date] = timestamp(refuelCreateDto.date.toString())
            it[volume] = refuelCreateDto.volume
            it[automobile] = refuelCreateDto.automobile
            it[trip] = refuelCreateDto.trip
            it[driver] = refuelCreateDto.driver
            it[billPhoto] = refuelCreateDto.billPhoto

        }.resultedValues ?: throw InternalServerException("Failed to create refuel")).first()
    }

    fun update(id: Int, refuelUpdateDto: RefuelUpdateDto): Boolean = transaction {
        RefuelModel.update({ RefuelModel.id eq id })
        {
            if (refuelUpdateDto.date != null){
                it[date] = timestamp(refuelUpdateDto.date.toString())
            }
            if (refuelUpdateDto.volume != null){
                it[volume] = refuelUpdateDto.volume
            }
            if (refuelUpdateDto.automobile != null){
                it[automobile] = refuelUpdateDto.automobile
            }
            if (refuelUpdateDto.trip != null){
                it[trip] = refuelUpdateDto.trip
            }
            if (refuelUpdateDto.driver != null){
                it[driver] = refuelUpdateDto.driver
            }
            if (refuelUpdateDto.billPhoto != null){
                it[billPhoto] = refuelUpdateDto.billPhoto
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        RefuelModel.deleteWhere{ RefuelModel.id eq id} != 0
    }
}