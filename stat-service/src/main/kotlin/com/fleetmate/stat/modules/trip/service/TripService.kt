package com.fleetmate.stat.modules.trip.service


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.dto.StatusDto
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.trip.dao.TripDao
import com.fleetmate.stat.modules.trip.dto.TripFilterDto
import com.fleetmate.stat.modules.car.dto.CarTripListItemDto
import com.fleetmate.stat.modules.user.dto.driver.DriverTripListItemDto
import com.fleetmate.stat.modules.trip.dto.TripListItemDto
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI

class TripService(di: DI) : KodeinService(di) {
    fun getAllStatuses(): List<StatusDto> =
        AppConf.TripStatus.entries.map {
            StatusDto(it.id, it.name)
        }

    //FIXME: Dao couldn`t handle nested where clauses (should be rewrote via Model API)
    fun getAllFiltered(tripFilterDto: TripFilterDto): List<TripListItemDto> = transaction {
        TripDao.find {
            with(tripFilterDto) { expressionBuilder }
        }.map {
            val output = it.listItemDto
            output.violations = TripDao.violations(output.id).count()
            output
        }
    }

    fun getByDriver(driverId: Int): List<DriverTripListItemDto> = transaction {
        TripDao.find {
            TripModel.driver eq driverId
        }.map {
            val output = it.listDriverDto
            output.violations = TripDao.violations(output.id).count()
            output
        }
    }

    fun getByCar(carId: Int): List<CarTripListItemDto> = transaction {
        TripDao.find {
            TripModel.car eq carId
        }.map {
            val output = it.listCarDto
            output.violations = TripDao.violations(output.id).count()
            output
        }
    }
}