package com.fleetmate.trip.modules.car.service

import com.fleetmate.lib.shared.modules.auth.dto.AuthorizedUser
import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.car.data.dao.CarDao
import com.fleetmate.trip.modules.car.data.dto.CarFullDto
import com.fleetmate.trip.modules.trip.data.dao.TripDao
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI

class CarService(di: DI) : KodeinService(di) {
    fun isAvailableForTrip(carId: Int): Boolean = transaction {
        val noCriticalFaults = FaultModel.select(FaultModel.id).where { FaultModel.car eq carId }.empty()
        val notInTrip = TripModel.select(TripModel.id).where { TripModel.car eq carId }.empty()

        noCriticalFaults && notInTrip
    }

    fun isNeedRefuel(carId: Int): Boolean = transaction {
        val carDao = CarDao[carId]

        carDao.fuelLevel <= 10
    }

    fun getCarByUser(authorizedUser: AuthorizedUser): CarFullDto = transaction {
        TripDao.getUserActiveTrip(authorizedUser.id).car.fullOutputDto
    }

    fun getOne(carId: Int): CarFullDto = transaction {
        CarDao[carId].fullOutputDto
    }
}