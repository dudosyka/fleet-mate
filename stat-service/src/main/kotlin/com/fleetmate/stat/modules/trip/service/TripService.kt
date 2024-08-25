package com.fleetmate.stat.modules.trip.service


import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.trip.dao.TripDao
import com.fleetmate.stat.modules.trip.dto.TripFilterDto
import com.fleetmate.stat.modules.car.dto.CarTripListItemDto
import com.fleetmate.stat.modules.user.dto.DriverTripListItemDto
import com.fleetmate.stat.modules.trip.dto.TripListItemDto
import org.kodein.di.DI

class TripService(di: DI) : KodeinService(di) {
    fun getAllFiltered(tripFilterDto: TripFilterDto): List<TripListItemDto> =
        TripDao.find {
            with(tripFilterDto) { expressionBuilder }
        }.map {
            val output = it.listItemDto
            output.violations = TripDao.violations(output.id).count()
            output
        }

    fun getByDriver(driverId: Int): List<DriverTripListItemDto> =
        TripDao.find {
            TripModel.driver eq driverId
        }.map {
            val output = it.listDriverDto
            output.violations = TripDao.violations(output.id).count()
            output
        }

    fun getByCar(carId: Int): List<CarTripListItemDto> =
        TripDao.find {
            TripModel.car eq carId
        }.map {
            val output = it.listCarDto
            output.violations = TripDao.violations(output.id).count()
            output
        }
}