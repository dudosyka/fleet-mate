package com.fleetmate.faults.modules.check.service

import com.fleetmate.faults.modules.check.data.dto.CheckDriverFinishDto
import com.fleetmate.faults.modules.check.data.dto.CheckFinishInputDto
import com.fleetmate.faults.modules.check.data.dto.CheckStartInputDto
import com.fleetmate.faults.modules.check.data.dto.CheckStartOutputDto
import com.fleetmate.faults.modules.faults.data.dto.FaultsUpdateDto
import com.fleetmate.faults.modules.faults.service.FaultsService
import com.fleetmate.faults.modules.photo.service.CarPhotoService
import com.fleetmate.lib.data.dto.car.CarUpdateDto
import com.fleetmate.lib.data.dto.car.CheckCreateDto
import com.fleetmate.lib.data.dto.car.CheckOutputDto
import com.fleetmate.lib.data.dto.report.ReportCreateDto
import com.fleetmate.lib.data.dto.trip.TripFinishDto
import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.data.model.car.CarTypeModel
import com.fleetmate.lib.data.model.report.ReportModel
import com.fleetmate.lib.dto.check.CheckUpdateDto
import com.fleetmate.lib.dto.trip.TripUpdateDto
import com.fleetmate.lib.exceptions.ForbiddenException
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.model.check.CheckModel
import com.fleetmate.lib.model.check.CheckModel.startTime
import com.fleetmate.lib.model.trip.TripModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.jetbrains.exposed.sql.ResultRow
import org.kodein.di.DI
import org.kodein.di.instance
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.collections.map

class CheckService(di: DI) : KodeinService(di) {

    private val carPhotoService: CarPhotoService by instance()
    private val faultsService: FaultsService by instance()

    fun getOne(id: Int): CheckOutputDto? {
        return CheckOutputDto(CheckModel.getOne(id) ?: return null)
    }

    fun getAll(): List<CheckOutputDto> {
        return CheckModel.getAll().map {
            CheckOutputDto(it)
        }
    }

    fun create(checkCreateDto: CheckCreateDto): CheckOutputDto =
        CheckOutputDto(CheckModel.create(checkCreateDto))

    fun update(id: Int, checkUpdateDto: CheckUpdateDto): Boolean =
        CheckModel.update(id, checkUpdateDto)

    fun delete(id: Int): Boolean =
        CheckModel.delete(id)

    fun mechanicBeforeStart(checkStartInputDto: CheckStartInputDto, userId: Int): CheckStartOutputDto {

        val check = start(checkStartInputDto, userId)
        val trip = getActiveTrip(checkStartInputDto.carId)
        TripModel.update(
            trip[TripModel.id].value,
            TripUpdateDto(
                mechanicCheckBeforeTrip = check.checkId
            )
        )
        return check
    }

    fun mechanicBeforeFinish(checkFinishInputDto: CheckFinishInputDto, userId: Int){
        val check = finish(checkFinishInputDto, userId)
        val trip = getActiveTrip(check[CheckModel.carId].value)
        val criticalFaults = faultsService.getAllCriticalByCar(check[CheckModel.carId].value)
        criticalFaults.forEach {
            faultsService.update(
                it.id!!,
                FaultsUpdateDto(
                    critical = false
                )
            )
        }
        TripModel.update(
            trip[TripModel.id].value,
            TripUpdateDto(
                mechanicCheckBeforeTrip = check[CheckModel.id].value,
                needWashing = checkFinishInputDto.needWash
            )
        )
    }

    fun mechanicAfterStart(checkStartInputDto: CheckStartInputDto, userId: Int): CheckStartOutputDto {

        val check = start(checkStartInputDto, userId)
        val trip = getActiveTrip(checkStartInputDto.carId)
        TripModel.update(
            trip[TripModel.id].value,
            TripUpdateDto(
                mechanicCheckAfterTrip = check.checkId
            )
        )
        return check
    }

    fun mechanicAfterFinish(checkFinishInputDto: CheckFinishInputDto, userId: Int) {
        val check = finish(checkFinishInputDto, userId)

        val trip = getActiveTrip(check[CheckModel.carId].value)
        TripModel.update(
            trip[TripModel.id].value,
            TripUpdateDto(
                mechanicCheckAfterTrip = check[CheckModel.id].value
            )
        )
    }

    fun driverBeforeStart(checkStartInputDto: CheckStartInputDto, userId: Int): CheckStartOutputDto {

        val check = start(checkStartInputDto, userId)

        val trip = getActiveTrip(checkStartInputDto.carId)
        TripModel.update(
            trip[TripModel.id].value,
            TripUpdateDto(
                driverCheckBeforeTrip = check.checkId
            )
        )
        return check
    }

    fun driverBeforeFinish(checkFinishInputDto: CheckFinishInputDto, userId: Int) {
        val check = finish(checkFinishInputDto, userId)

        val trip = getActiveTrip(check[CheckModel.carId].value)
        TripModel.update(
            trip[TripModel.id].value,
            TripUpdateDto(
                driverCheckAfterTrip = check[CheckModel.id].value
            )
        )
    }

    fun driverAfterStart(checkStartInputDto: CheckStartInputDto, userId: Int): CheckStartOutputDto {

        val check = start(checkStartInputDto, userId)

        val trip = getActiveTrip(checkStartInputDto.carId)
        TripModel.update(
            trip[TripModel.id].value,
            TripUpdateDto(
                driverCheckAfterTrip = check.checkId
            )
        )
        return check
    }

    fun driverAfterFinish(checkDriverFinishInputDto: CheckDriverFinishDto, userId: Int) {

        val check = finish(
            CheckFinishInputDto(
                checkId = checkDriverFinishInputDto.checkId,
                photos = checkDriverFinishInputDto.photos
        ), userId)

        val trip = getActiveTrip(check[CheckModel.carId].value)


        finishTrip(
            TripFinishDto(
                trip[TripModel.id].value,
                trip[TripModel.car].value,
                checkDriverFinishInputDto.mileage
            ),
            userId
        )
        TripModel.update(
            trip[TripModel.id].value,
            TripUpdateDto(
                driverCheckAfterTrip = check[CheckModel.id].value
            )
        )
    }

    fun finishTrip(tripFinishDto: TripFinishDto, userId: Int) {
        val trip = getActiveTrip(tripFinishDto.carId)

        val car = CarModel.getOne(tripFinishDto.carId) ?: throw NotFoundException("Car is not found")
        val tripMileage = tripFinishDto.mileage - car[CarModel.mileage]
        val fuel = car[CarModel.fuelLevel] - ((tripMileage / 100) / car[CarTypeModel.avgFuelConsumption])
        CarModel.update(
            tripFinishDto.carId,
            CarUpdateDto(
                mileage = tripFinishDto.mileage,
                fuelLevel = fuel
            )
        )
        ReportModel.create(
            ReportCreateDto(
                mileage = tripMileage,
                avgSpeed = trip[TripModel.avgSpeed]!!,
                trip = trip[TripModel.id].value,
                car = tripFinishDto.carId,
                driver = userId
            )
        )
    }

    fun start(checkStartInputDto: CheckStartInputDto, userId: Int) =
        CheckStartOutputDto(
            CheckModel.start(
                userId,
                checkStartInputDto.carId
            )
        )


    fun finish(checkFinishInputDto: CheckFinishInputDto, userId: Int): ResultRow {
        val check = CheckModel.getCheckForFinish(checkFinishInputDto.checkId)
        if (check[CheckModel.author].value == userId){
            carPhotoService.uploadPhotos(userId, check[CheckModel.carId].value, checkFinishInputDto.photos)
            val finishTime = LocalDateTime.now()
            var exceed = false
            if (check[startTime].epochSecond < finishTime.minusMinutes(15).toEpochSecond(ZoneOffset.UTC)){
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
    fun getActiveTrip(carId: Int): ResultRow{
        val trip = TripModel.getActiveTrip(carId)
        trip?: throw NotFoundException("Active Trip with this car is not found")
        return trip
    }
}