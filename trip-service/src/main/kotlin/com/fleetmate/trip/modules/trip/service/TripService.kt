package com.fleetmate.trip.modules.trip.service.trip

import com.fleetmate.lib.data.dto.trip.TripInitDto
import com.fleetmate.lib.data.dto.trip.TripWashInputDto
import com.fleetmate.lib.data.dto.violation.ViolationCreateDto
import com.fleetmate.lib.dto.automobile.AutomobileUpdateDto
import com.fleetmate.lib.dto.trip.TripCreateDto
import com.fleetmate.lib.dto.trip.TripFullOutputDto
import com.fleetmate.lib.dto.trip.TripOutputDto
import com.fleetmate.lib.dto.trip.TripUpdateDto
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.model.trip.TripModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.automobile.service.AutomobileService
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
    private val automobileService: AutomobileService by instance()

    fun getOne(id: Int): TripFullOutputDto? {
        return TripFullOutputDto(TripModel.getOne(id) ?: return null)
    }

    fun getActiveTrip(automobileId: Int): ResultRow {
        val trip = TripModel.getActiveTrip(automobileId)
        trip?: throw NotFoundException("Active Trip with this automobile is not found")
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

    fun getTripInfo(tripDriverInputDto: TripDriverInputDto) =
        TripModel.getAllDriverTrip(tripDriverInputDto.driverId, tripDriverInputDto.day)

    fun initTrip(tripInitDto: TripInitDto): TripInitOutputDto {
        val refuel = automobileService.checkFuel(tripInitDto.automobileId)
        val trip = TripOutputDto(TripModel.initTrip(tripInitDto.driverId, tripInitDto.automobileId))
        return TripInitOutputDto(
            refuel,
            trip
        )
    }

    fun finishTrip(tripFinishDto: TripFinishDto) {
        val trip = getActiveTrip(tripFinishDto.automobileId)

        val auto = automobileService.getOne(tripFinishDto.automobileId) ?: throw NotFoundException("Automobile is not found")
        val tripMileage = tripFinishDto.mileage - auto.mileage!!
        val fuel = auto.fuelLevel!! - (tripMileage / auto.type?.avgFuelConsumption!!)
        automobileService.update(
            tripFinishDto.automobileId,
            AutomobileUpdateDto(
                mileage = tripFinishDto.mileage,
                fuelLevel = fuel
            )
        )
        update(
            trip[TripModel.id].value,
            TripUpdateDto(
                keyReturn = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            )
        )
    }

    fun setNeedWash(washInputDto: TripWashInputDto) {
        val trip = getActiveTrip(washInputDto.automobileId)
        TripModel.update(
            trip[TripModel.id].value,
            TripUpdateDto(
                washHappen = washInputDto.wash
            )
        )
    }

    fun setWash(washInitDto: TripWashInputDto) {
        val trip = getActiveTrip(washInitDto.automobileId)
        TripModel.update(
            trip[TripModel.id].value,
            TripUpdateDto(
                washHappen = washInitDto.wash
            )
        )
    }
    fun checkWash(automobileId: Int) {
        val trip = getActiveTrip(automobileId)

        if (trip[TripModel.washHappen] == false){
            violationService.create(
                ViolationCreateDto(
                    date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                    type = 1, //FIXME
                    duration = (0).toFloat(),
                    hidden = false,
                    driver = trip[TripModel.driver].value,
                    automobile = automobileId,
                    comment = "There was no automobile wash",
                    trip = trip[TripModel.id].value
                )
            )
        }
    }
}