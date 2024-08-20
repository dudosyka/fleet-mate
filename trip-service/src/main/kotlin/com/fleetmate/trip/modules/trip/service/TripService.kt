package com.fleetmate.trip.modules.trip.service.trip

import com.fleetmate.lib.conf.AppConf
import com.fleetmate.lib.data.dto.car.CarUpdateDto
import com.fleetmate.lib.data.dto.trip.TripInitDto
import com.fleetmate.lib.data.dto.trip.TripWashInputDto
import com.fleetmate.lib.data.dto.violation.ViolationCreateDto
import com.fleetmate.lib.dto.auth.AuthorizedUser
import com.fleetmate.lib.dto.trip.TripCreateDto
import com.fleetmate.lib.dto.trip.TripFullOutputDto
import com.fleetmate.lib.dto.trip.TripOutputDto
import com.fleetmate.lib.dto.trip.TripUpdateDto
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.model.trip.TripModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.car.service.CarService
import com.fleetmate.trip.modules.report.data.dto.ReportCreateDto
import com.fleetmate.trip.modules.report.service.ReportService
import com.fleetmate.trip.modules.trip.data.dto.TripDriverInputDto
import com.fleetmate.trip.modules.trip.data.dto.TripFinishDto
import com.fleetmate.trip.modules.trip.data.dto.TripInitOutputDto
import com.fleetmate.trip.modules.violation.service.ViolationService
import org.jetbrains.exposed.sql.ResultRow
import org.kodein.di.DI
import org.kodein.di.instance
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.collections.map

class TripService(di: DI) : KodeinService(di) {

    private val violationService: ViolationService by instance()
    private val carService: CarService by instance()
    private val reportService: ReportService by instance()

    fun getOne(id: Int): TripFullOutputDto? {
        return TripFullOutputDto(TripModel.getOne(id) ?: return null)
    }

    fun getActiveTrip(carId: Int): ResultRow {
        val trip = TripModel.getActiveTrip(carId)
        trip?: throw NotFoundException("Active Trip with this car is not found")
        return trip
    }

    fun getAll(): List<TripOutputDto> {
        return TripModel.getAll().map {
            TripOutputDto(it)
        }
    }

    fun create(tripCreateDto: TripCreateDto): TripOutputDto =
        TripOutputDto(TripModel.create(tripCreateDto))

    fun update(id: Int, tripUpdateDto: TripUpdateDto): Boolean =
        TripModel.update(id, tripUpdateDto)

    fun delete(id: Int): Boolean =
        TripModel.delete(id)

    fun getTripInfo(tripDriverInputDto: TripDriverInputDto, authorizedUser: AuthorizedUser) =
        TripModel.getAllDriverTrip(authorizedUser.id, tripDriverInputDto.day)

    fun initTrip(tripInitDto: TripInitDto, authorizedUser: AuthorizedUser): TripInitOutputDto {
        val refuel = carService.checkFuel(tripInitDto.carId)
        val trip = TripOutputDto(TripModel.initTrip(authorizedUser.id, tripInitDto.carId))
        return TripInitOutputDto(
            refuel,
            trip
        )
    }

    fun finishTrip(tripFinishDto: TripFinishDto, authorizedUser: AuthorizedUser) {
        val trip = getActiveTrip(tripFinishDto.carId)

        val car = carService.getOne(tripFinishDto.carId) ?: throw NotFoundException("Car is not found")
        val tripMileage = tripFinishDto.mileage - car.mileage!!
        val fuel = car.fuelLevel!! - ((tripMileage / 100) / car.type?.avgFuelConsumption!!)
        carService.update(
            tripFinishDto.carId,
            CarUpdateDto(
                mileage = tripFinishDto.mileage,
                fuelLevel = fuel
            )
        )
        reportService.create(
            ReportCreateDto(
                mileage = tripMileage,
                avgSpeed = trip[TripModel.avgSpeed]!!,
                trip = trip[TripModel.id].value,
                car = tripFinishDto.carId,
                driver = authorizedUser.id
            )
        )
    }

    fun setNeedWash(washInputDto: TripWashInputDto) {
        val trip = getActiveTrip(washInputDto.carId)
        TripModel.update(
            trip[TripModel.id].value,
            TripUpdateDto(
                needWashing = washInputDto.wash
            )
        )
    }

    fun setWash(washInitDto: TripWashInputDto) {
        val trip = getActiveTrip(washInitDto.carId)
        TripModel.update(
            trip[TripModel.id].value,
            TripUpdateDto(
                washHappen = washInitDto.wash
            )
        )
    }
    fun checkWash(carId: Int) {
        val trip = getActiveTrip(carId)

        if (trip[TripModel.washHappen] == false){
            violationService.create(
                ViolationCreateDto(
                    date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                    type = AppConf.ViolationType.DEFAULT.id,
                    duration = (0).toFloat(),
                    hidden = false,
                    driver = trip[TripModel.driver].value,
                    car = carId,
                    comment = "There was no car wash",
                    trip = trip[TripModel.id].value
                )
            )
        }
    }
    fun keyReturn(carId: Int){
        val trip = getActiveTrip(carId)

        update(
            trip[TripModel.id].value,
            TripUpdateDto(
                keyReturn = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            )
        )
    }
}