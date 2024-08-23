package com.fleetmate.faults.modules.check.service

import com.fleetmate.faults.modules.check.dao.CheckDao
import com.fleetmate.faults.modules.check.dto.FinishCheckInputDto
import com.fleetmate.faults.modules.fault.data.dao.FaultDao
import com.fleetmate.faults.modules.trip.TripDao
import com.fleetmate.lib.exceptions.BadRequestException
import com.fleetmate.lib.exceptions.ForbiddenException
import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.modules.auth.dto.AuthorizedUser
import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.photo.service.PhotoService
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.lib.utils.kodein.KodeinService
import io.ktor.util.date.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.instance

class CheckService(di: DI) : KodeinService(di) {
    private val photoService: PhotoService by instance()
    fun start(authorizedUser: AuthorizedUser, carId: Int) = transaction {
        val check = CheckDao.new {
            authorId = EntityID(authorizedUser.id, UserModel)
            this.carId = EntityID(carId, CarModel)
            startTime = getTimeMillis()
        }
        check.flush()

        check.toOutputDto()
    }

    private fun finish(authorizedUser: AuthorizedUser, carId: Int): CheckDao = transaction {
        val check = CheckDao.findActiveCheckByUser(carId, authorizedUser.id)

        if (check.finishTime != null)
            throw BadRequestException("Bad check provided")

        val finishTime = getTimeMillis()

        check.finishTime = finishTime
        check.timeExceeded = (finishTime - check.startTime) >= (15 * 60 * 1000)

        check.flush()
        check
    }

    fun finishDriverCheckBeforeTrip(authorizedUser: AuthorizedUser, finishCheckInputDto: FinishCheckInputDto) = transaction {
        val trip = TripDao.getCarActiveTrip(finishCheckInputDto.carId)

        if (!trip.driverCheckBeforeTripCanBeFinished)
            throw ForbiddenException()

        if (finishCheckInputDto.photos.size < 5)
            throw BadRequestException("To complete check provide more photos (5 is minimum)")

        trip.driverCheckBeforeTrip = finish(authorizedUser, finishCheckInputDto.carId)
        trip.appendCarPhotos(trip.driverCheckBeforeTrip?.idValue, photoService.upload(finishCheckInputDto.photos.map { it.type = AppConf.PhotoType.CAR; it }))
        trip.flush()
    }

    fun finishMechanicCheckBeforeTrip(authorizedUser: AuthorizedUser, finishCheckInputDto: FinishCheckInputDto) = transaction {
        val trip = TripDao.getCarActiveTrip(finishCheckInputDto.carId)

        if (!trip.mechanicCheckBeforeTripCanBeFinished)
            throw ForbiddenException()

        if (!finishCheckInputDto.approved)
            trip.status = AppConf.TripStatus.CLOSED_DUE_TO_FAULT.name
        else {
            FaultDao.setNotCriticalForCar(trip.carId.value)
            trip.status = AppConf.TripStatus.EXPLOITATION.name
        }

        trip.needWashing = finishCheckInputDto.needWash
        trip.mechanicCheckBeforeTrip = finish(authorizedUser, finishCheckInputDto.carId)
        trip.flush()
    }

    fun finishMechanicCheckAfterTrip(authorizedUser: AuthorizedUser, finishCheckInputDto: FinishCheckInputDto) = transaction {
        val trip = TripDao.getCarActiveTrip(finishCheckInputDto.carId)

        if (!trip.mechanicCheckAfterTripCanBeFinished)
            throw ForbiddenException()

        trip.mechanicCheckAfterTrip = finish(authorizedUser, finishCheckInputDto.carId)
        trip.flush()
    }

    fun finishDriverCheckAfterTrip(authorizedUser: AuthorizedUser, finishCheckInputDto: FinishCheckInputDto) = transaction {
        val trip = TripDao.getCarActiveTrip(finishCheckInputDto.carId)

        if (!trip.driverCheckAfterTripCanBeFinished)
            throw ForbiddenException()

        if (finishCheckInputDto.photos.size < 5)
            throw BadRequestException("To complete check provide more photos (5 is minimum)")

        if (finishCheckInputDto.mileage == 0.0)
            throw BadRequestException("To complete check provide correct mileage")

        trip.status = AppConf.TripStatus.CLOSED.name
        trip.driverCheckAfterTrip = finish(authorizedUser, finishCheckInputDto.carId)
        trip.mileage = finishCheckInputDto.mileage
        trip.appendCarPhotos(trip.driverCheckAfterTrip?.idValue, photoService.upload(finishCheckInputDto.photos.map { it.type = AppConf.PhotoType.CAR; it }))
        trip.flush()
    }
}