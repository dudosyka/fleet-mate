package com.fleetmate.trip.modules.trip.service

import com.fleetmate.lib.exceptions.BadRequestException
import com.fleetmate.lib.exceptions.ForbiddenException
import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.car.data.dao.CarDao
import com.fleetmate.trip.modules.car.service.CarService
import com.fleetmate.trip.modules.trip.data.dao.TripDao
import com.fleetmate.trip.modules.trip.data.dto.TripDto
import com.fleetmate.trip.modules.trip.data.dto.TripInitDto
import com.fleetmate.trip.modules.user.service.UserService
import com.fleetmate.trip.modules.violation.service.ViolationService
import io.ktor.util.date.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.instance

class TripService(di: DI) : KodeinService(di) {
    private val carService: CarService by instance()
    private val userService: UserService by instance()
    private val violationService: ViolationService by instance()

    suspend fun initTrip(tripInitDto: TripInitDto): TripDto {
        if (!carService.isAvailableForTrip(tripInitDto.carId))
            throw ForbiddenException()

        val licenceType = transaction {
            CarDao[tripInitDto.carId].licenceType
        }

        if (!userService.isAvailableForTrip(tripInitDto.driverId, licenceType))
            throw ForbiddenException()

        return transaction {
            TripDao.init(tripInitDto.carId, tripInitDto.driverId, needRefuel = carService.isNeedRefuel(tripInitDto.carId)).toOutputDto()
        }
    }

    fun finishTrip(driverId: Int): TripDto = transaction {
        val trip = TripDao.getUserActiveTrip(driverId)

        if (trip.status == AppConf.TripStatus.CLOSED_DUE_TO_FAULT.name) {
            trip.keyReturn = getTimeMillis()
            return@transaction trip.toOutputDto()
        }

        if (!trip.canBeClosed)
            throw BadRequestException("Bad trip provided")

        trip.keyReturn = getTimeMillis()
        trip.status = AppConf.TripStatus.CLOSED.name

        if (trip.needRefuel && !trip.isRefueled)
            violationService.registerRefuelViolation(trip)

        if (trip.needWashing && !trip.isWashed)
            violationService.registerWashViolation(trip)

        trip.updateCarByMileageAndFuelLevel(
            trip.car.mileage + trip.mileage,
            trip.car.fuelLevel - (trip.mileage / 100) * trip.car.avgFuelConsumption
        )

        trip.car.flush()
        trip.flush()
        trip.toOutputDto()
    }

}