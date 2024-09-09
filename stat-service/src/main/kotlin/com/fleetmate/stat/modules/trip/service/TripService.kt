package com.fleetmate.stat.modules.trip.service


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.dto.StatusDto
import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.car.model.type.CarTypeModel
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.shared.modules.violation.model.ViolationModel
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.car.dao.CarDao
import com.fleetmate.stat.modules.car.dao.CarTypeDao.Companion.nullableRangeCond
import com.fleetmate.stat.modules.car.dao.CarTypeDao.Companion.rangeCond
import com.fleetmate.stat.modules.car.dto.CarFilterDto
import com.fleetmate.stat.modules.trip.dao.TripDao
import com.fleetmate.stat.modules.trip.dto.TripFilterDto
import com.fleetmate.stat.modules.car.dto.CarTripListItemDto
import com.fleetmate.stat.modules.user.dto.driver.DriverTripListItemDto
import com.fleetmate.stat.modules.trip.dto.TripListItemDto
import com.fleetmate.stat.modules.user.dao.UserDao
import com.fleetmate.stat.modules.user.dto.filter.StaffFilterDto
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI

class TripService(di: DI) : KodeinService(di) {
    fun getAllStatuses(): List<StatusDto> =
        AppConf.TripStatus.entries.map {
            StatusDto(it.id, it.name)
        }

    fun getAllFiltered(tripFilterDto: TripFilterDto): List<TripListItemDto> = transaction {
        TripModel
            .join(CarModel, JoinType.INNER, TripModel.car, CarModel.id) {
                with(tripFilterDto.carFilter ?: CarFilterDto()) { expressionBuilder }
            }
            .innerJoin(CarTypeModel)
            .join(UserModel, JoinType.INNER, TripModel.driver, UserModel.id) {
                with(tripFilterDto.driverFilter ?: StaffFilterDto()) { expressionBuilder }
            }
            .join(ViolationModel, JoinType.LEFT, ViolationModel.trip, TripModel.id)
            .select(
                TripModel.id, TripModel.status, TripModel.keyAcceptance, TripModel.keyReturn,
                UserModel.id, UserModel.fullName,
                CarModel.id, CarModel.name, CarModel.registrationNumber,
                CarTypeModel.name, ViolationModel.id.count()
            )
            .groupBy(TripModel.id, UserModel.id, CarModel.id, CarTypeModel.name)
            .where {
                rangeCond(tripFilterDto.startDate, TripModel.id neq 0, TripModel.keyAcceptance, Long.MIN_VALUE, Long.MAX_VALUE) and
                nullableRangeCond(tripFilterDto.endDate, TripModel.id neq 0, TripModel.keyReturn,  Long.MIN_VALUE, Long.MAX_VALUE)
            }
            .map {
                val userDao = UserDao.wrapRow(it)
                val carDao = CarDao.wrapRow(it)
                val tripDao = TripDao.wrapRow(it)
                TripListItemDto(
                    tripDao.idValue,
                    tripDao.status,
                    tripDao.keyAcceptance,
                    tripDao.keyReturn,
                    userDao.simpleDto,
                    carDao.simpleDto(it[CarTypeModel.name]),
                    it[ViolationModel.id.count()]
                )
            }

//        TripDao.find {
//            with(tripFilterDto) { expressionBuilder }
//        }.map {
//            val output = it.listItemDto
//            output.violations = TripDao.violations(output.id).count()
//            output
//        }
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