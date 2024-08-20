package com.fleetmate.faults.modules.check.service

import com.fleetmate.faults.modules.check.data.dto.CheckFinishInputDto
import com.fleetmate.faults.modules.check.data.dto.CheckStartInputDto
import com.fleetmate.faults.modules.check.data.dto.CheckStartOutputDto
import com.fleetmate.faults.modules.photo.service.CarPhotoService
import com.fleetmate.lib.data.dto.car.CheckCreateDto
import com.fleetmate.lib.data.dto.car.CheckOutputDto
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

    fun mechanicStart(checkStartInputDto: CheckStartInputDto, userId: Int): CheckStartOutputDto {

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

    fun mechanicFinish(checkFinishInputDto: CheckFinishInputDto, userId: Int) {
        val check = finish(checkFinishInputDto, userId)

        val trip = getActiveTrip(check[CheckModel.carId].value)
        TripModel.update(
            trip[TripModel.id].value,
            TripUpdateDto(
                mechanicCheckAfterTrip = check[CheckModel.id].value
            )
        )
    }

    fun driverStart(checkStartInputDto: CheckStartInputDto, userId: Int): CheckStartOutputDto {

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

    fun driverFinish(checkFinishInputDto: CheckFinishInputDto, userId: Int) {

        val check = finish(checkFinishInputDto, userId)

        val trip = getActiveTrip(check[CheckModel.carId].value)
        TripModel.update(
            trip[TripModel.id].value,
            TripUpdateDto(
                driverCheckAfterTrip = check[CheckModel.id].value
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