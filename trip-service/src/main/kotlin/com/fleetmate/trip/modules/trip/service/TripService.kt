package com.fleetmate.trip.modules.trip.service

import com.fleetmate.lib.data.dto.car.CarIdDto
import com.fleetmate.lib.data.dto.trip.TripInitDto
import com.fleetmate.lib.data.dto.trip.TripWashInputDto
import com.fleetmate.lib.data.dto.auth.AuthorizedUser
import com.fleetmate.lib.data.dto.trip.TripFullOutputDto
import com.fleetmate.lib.data.dto.trip.TripOutputDto
import com.fleetmate.lib.data.dto.trip.TripUpdateDto
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.data.model.trip.TripModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.lib.exceptions.ForbiddenException
import com.fleetmate.trip.modules.car.service.CarService
import com.fleetmate.trip.modules.user.service.UserService
import com.fleetmate.trip.modules.violation.service.ViolationService
import org.kodein.di.DI
import org.kodein.di.instance
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.collections.map

class TripService(di: DI) : KodeinService(di) {
    private val violationService: ViolationService by instance()
    private val carService: CarService by instance()
    private val userService: UserService by instance()

    fun getOne(id: Int): TripFullOutputDto {
        return TripFullOutputDto(TripModel.getOne(id) ?: throw NotFoundException("Trip not found"))
    }

    fun getActiveTrip(carId: Int): TripOutputDto {
        val trip = TripModel.getActiveTrip(carId) ?: throw NotFoundException("Active Trip with this car is not found")
        return TripOutputDto(trip)
    }

    fun getAll(): List<TripOutputDto> {
        return TripModel.getAll().map {
            TripOutputDto(it)
        }
    }

    fun getTripInfo(authorizedUser: AuthorizedUser) =
        TripModel.getAllDriverTrip(authorizedUser.id)

    suspend fun initTrip(tripInitDto: TripInitDto, authorizedUser: AuthorizedUser): TripOutputDto {
        if (!carService.isAvailable(tripInitDto.carId))
            throw ForbiddenException()

        if (!userService.isCheckupCompleted(authorizedUser.id))
            throw ForbiddenException()

        return TripOutputDto(TripModel.create(authorizedUser.id, tripInitDto.carId))
    }

    fun finishTrip(carIdDto: CarIdDto) {
        val trip = getActiveTrip(carIdDto.id)
        val driverBefore = trip.driverCheckBeforeTrip
        val mechanicBefore = trip.mechanicCheckBeforeTrip
        val driverAfter = trip.driverCheckAfterTrip
        val mechanicAfter = trip.mechanicCheckAfterTrip

        if (driverBefore == null || mechanicBefore == null || driverAfter == null || mechanicAfter == null){
            throw ForbiddenException()
        }

        TripModel.update(
            trip.id,
            TripUpdateDto(
                keyReturn = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            )
        )
    }

    fun setNeedWash(washInputDto: TripWashInputDto) {
        val trip = getActiveTrip(washInputDto.carId)
        TripModel.update(
            trip.id,
            TripUpdateDto(
                needWashing = washInputDto.wash
            )
        )
    }

    fun setWash(washInitDto: TripWashInputDto) {
        val trip = getActiveTrip(washInitDto.carId)
        TripModel.update(
            trip.id,
            TripUpdateDto(
                washHappen = washInitDto.wash
            )
        )
    }

    fun checkWash(carId: Int) {
        val trip = getActiveTrip(carId)

        if (trip.washHappen != null && trip.needWashing != null)
            violationService.createNoWash(trip)
    }
}