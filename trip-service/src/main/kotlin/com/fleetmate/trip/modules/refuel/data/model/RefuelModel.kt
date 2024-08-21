package com.fleetmate.trip.modules.refuel.data.model

import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.data.model.photo.PhotoModel
import com.fleetmate.lib.data.model.trip.TripModel
import com.fleetmate.lib.data.model.user.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import com.fleetmate.trip.modules.refuel.data.dto.RefuelCreateDto
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList


object RefuelModel : BaseIntIdTable() {
    val volume = double("volume")
    val car = reference("car", CarModel)
    val trip = reference("trip", TripModel)
    val driver = reference("driver", UserModel)
    val billPhoto = reference("bill photo", PhotoModel)

    fun getOne(id: Int): ResultRow? = transaction {
        select(RefuelModel.id, createdAt, volume, car, trip, driver, billPhoto).where {
            RefuelModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(RefuelModel.id, createdAt, volume, car, trip, driver, billPhoto).toList()
    }

    fun create(refuelCreateDto: RefuelCreateDto): ResultRow = transaction {
        (RefuelModel.insert {
            it[volume] = refuelCreateDto.volume
            it[car] = refuelCreateDto.carId
            it[trip] = refuelCreateDto.tripId
            it[driver] = refuelCreateDto.driverId
            it[billPhoto] = refuelCreateDto.billPhoto

        }.resultedValues ?: throw InternalServerException("Failed to create refuel")).first()
    }
}