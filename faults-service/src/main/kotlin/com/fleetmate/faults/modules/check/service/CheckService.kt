package com.fleetmate.faults.modules.check.service

import com.fleetmate.faults.modules.check.data.dto.CheckDriverFinishDto
import com.fleetmate.faults.modules.check.data.dto.CheckFinishInputDto
import com.fleetmate.faults.modules.check.data.dto.CheckStartInputDto
import com.fleetmate.faults.modules.check.data.dto.CheckStartOutputDto
import com.fleetmate.lib.data.model.faults.FaultsModel
import com.fleetmate.faults.modules.photo.service.CarPhotoService
import com.fleetmate.lib.conf.AppConf
import com.fleetmate.lib.data.dto.car.CarUpdateDto
import com.fleetmate.lib.data.dto.car.photo.CarPhotoCreateDto
import com.fleetmate.lib.data.dto.check.CheckOutputDto
import com.fleetmate.lib.data.dto.report.ReportCreateDto
import com.fleetmate.lib.data.dto.trip.TripOutputDto
import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.data.model.car.CarTypeModel
import com.fleetmate.lib.data.model.report.ReportModel
import com.fleetmate.lib.data.dto.trip.TripUpdateDto
import com.fleetmate.lib.exceptions.ForbiddenException
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.data.model.check.CheckModel
import com.fleetmate.lib.data.model.check.CheckModel.startTime
import com.fleetmate.lib.data.model.trip.TripModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.jetbrains.exposed.sql.ResultRow
import org.kodein.di.DI
import org.kodein.di.instance
import java.time.LocalDateTime
import java.time.ZoneOffset

class CheckService(di: DI) : KodeinService(di) {
    private val carPhotoService: CarPhotoService by instance()

    fun getOne(id: Int): CheckOutputDto? {
        return CheckOutputDto(CheckModel.getOne(id) ?: return null)
    }

    fun start(checkStartInputDto: CheckStartInputDto, userId: Int) =
        CheckStartOutputDto(
            CheckModel.start(
                userId,
                checkStartInputDto.carId
            )
        )

    private fun finish(checkFinishInputDto: CheckFinishInputDto, userId: Int): ResultRow {
        val check = CheckModel.getCheckForFinish(checkFinishInputDto.checkId)
        val trip = getActiveTrip(check[CheckModel.carId].value)

        if (check[CheckModel.author].value == userId){
            carPhotoService.uploadPhotos(CarPhotoCreateDto(trip.car, trip.id, trip.driver, checkFinishInputDto.photos))
            val finishTime = LocalDateTime.now()
            var exceed = false
            if (check[startTime] < finishTime.minusMinutes(15).toEpochSecond(ZoneOffset.UTC)){
                exceed = true
            }

            CheckModel.finish(
                checkFinishInputDto.checkId,
                exceed
            )

            return check
        }else{
            throw ForbiddenException()
        }
    }

    private fun getActiveTrip(carId: Int): TripOutputDto {
        val trip = TripModel.getActiveTrip(carId) ?: throw NotFoundException("Active Trip with this car is not found")
        return TripOutputDto(trip)
    }

    //TODO: Добавить эмиты в сокеты на мойку и на отмену поездки
    fun completeMechanicBeforeTrip(checkFinishInputDto: CheckFinishInputDto, userId: Int) {
        val check = finish(checkFinishInputDto, userId)
        val trip = getActiveTrip(check[CheckModel.carId].value)

        TripModel.update(trip.id, TripUpdateDto(
            mechanicCheckBeforeTrip = check[CheckModel.id].value,
            needWashing = checkFinishInputDto.needWash
        )
        )

        if (checkFinishInputDto.approved)
            FaultsModel.markAllAsNotCriticalForCar(trip.id)
        else
            TripModel.update(trip.id, TripUpdateDto(
                status = AppConf.TripStatus.CLOSED_DUE_TO_FAULTS
            )
            )
    }

    fun completeMechanicAfterTrip(checkFinishInputDto: CheckFinishInputDto, userId: Int) {
        val check = finish(checkFinishInputDto, userId)

        val trip = getActiveTrip(check[CheckModel.carId].value)
        TripModel.update(
            trip.id,
            TripUpdateDto(
                mechanicCheckAfterTrip = check[CheckModel.id].value
            )
        )
    }

    fun completeDriverBeforeTrip(checkFinishInputDto: CheckFinishInputDto, userId: Int) {
        val check = finish(checkFinishInputDto, userId)

        val trip = getActiveTrip(check[CheckModel.carId].value)
        TripModel.update(
            trip.id,
            TripUpdateDto(
                driverCheckBeforeTrip = check[CheckModel.id].value
            )
        )
    }

    fun completeDriverAfterTrip(checkDriverFinishInputDto: CheckDriverFinishDto, userId: Int) {
        val check = finish(
            CheckFinishInputDto(
                checkId = checkDriverFinishInputDto.checkId,
                photos = checkDriverFinishInputDto.photos
            ), userId)

        val trip = getActiveTrip(check[CheckModel.carId].value)

        val car = CarModel.getOne(trip.car) ?: throw NotFoundException("Car is not found")
        val tripMileage = checkDriverFinishInputDto.mileage - car[CarModel.mileage]
        val fuel = car[CarModel.fuelLevel] - ((tripMileage / 100) / car[CarTypeModel.avgFuelConsumption])
        CarModel.update(
            trip.car,
            CarUpdateDto(
                mileage = checkDriverFinishInputDto.mileage,
                fuelLevel = fuel
            )
        )
        ReportModel.create(
            ReportCreateDto(
                mileage = tripMileage,
                avgSpeed = trip.avgSpeed ?: 0.0,
                trip = trip.id,
                car = trip.car,
                driver = userId
            )
        )

        TripModel.update(
            trip.id,
            TripUpdateDto(
                driverCheckAfterTrip = check[CheckModel.id].value
            )
        )
    }
}