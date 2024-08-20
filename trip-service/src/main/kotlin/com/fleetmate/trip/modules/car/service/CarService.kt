package com.fleetmate.trip.modules.car.service

import com.fleetmate.lib.data.dto.car.CarCreateDto
import com.fleetmate.lib.data.dto.car.CarFullOutputDto
import com.fleetmate.lib.data.dto.car.CarUpdateDto
import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.data.model.car.CarPartModel
import com.fleetmate.lib.dto.car.CarOutputDto
import com.fleetmate.lib.dto.photo.PhotoCreateDto
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.model.trip.TripModel
import com.fleetmate.lib.utils.files.FilesUtil
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.car.data.dto.CarRefuelDto
import com.fleetmate.trip.modules.photo.service.PhotoService
import com.fleetmate.trip.modules.refuel.data.dto.RefuelCreateDto
import com.fleetmate.trip.modules.refuel.data.dto.RefuelInputDto
import com.fleetmate.trip.modules.refuel.data.model.RefuelModel
import com.fleetmate.trip.modules.trip.service.trip.TripService
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.instance
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.collections.map

class CarService(di: DI) : KodeinService(di) {

    private val carPartService: CarPartService by instance()
    private val photoService: PhotoService by instance()
    private val tripService: TripService by instance()

    fun getOne(id: Int): CarOutputDto? {
        return CarOutputDto(CarModel.getOne(id) ?: return null)
    }

    fun getAll(): List<CarOutputDto> {
        return CarModel.getAll().map {
            CarOutputDto(it)
        }
    }

    fun create(carCreateDto: CarCreateDto): CarOutputDto =
        CarOutputDto(CarModel.create(carCreateDto))

    fun update(id: Int, carUpdateDto: CarUpdateDto): Boolean =
        CarModel.update(id, carUpdateDto)

    fun delete(id: Int): Boolean =
        CarModel.delete(id)

    fun getFullInfo(id: Int): CarFullOutputDto {
        val automobile = getOne(id)
        val root = carPartService.getOne(automobile?.type?.id)
        if (root != null && automobile != null){
            val parts = CarPartModel.getTreeFrom(
                root
            )
            return CarFullOutputDto(
                automobile,
                parts
            )
        }
        throw NotFoundException("Information about automobile or automobile parts is not found")
    }
    fun checkFuel(automobileId: Int): CarRefuelDto {
        val auto = CarModel.getOne(automobileId) ?: throw NotFoundException(
            "Automobile is not found"
        )
        return if (auto[CarModel.fuelLevel] <= 10) {
            CarRefuelDto(
                true
            )
        }else{
            CarRefuelDto(
                false
            )
        }
    }
    fun refuel(refuelInputDto: RefuelInputDto) = transaction{

        val photoName = FilesUtil.buildName(refuelInputDto.car.toString() + LocalDateTime.now().toString())
        val compressImageName = FilesUtil.buildName("shakal" + refuelInputDto.car.toString() + LocalDateTime.now().toString())


        val image = photoService.create(
            PhotoCreateDto(
                link = photoName,
                date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                type = "refuel" //FIXME
            )
        )
        photoService.create(
            PhotoCreateDto(
                link = compressImageName,
                date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                type = "refuel" //FIXME
            )
        )
        FilesUtil.upload(refuelInputDto.billPhoto, photoName, compressImageName)

        val trip = tripService.getActiveTrip(refuelInputDto.car)

        RefuelModel.create(
            RefuelCreateDto(
                refuelInputDto.date,
                refuelInputDto.volume,
                refuelInputDto.car,
                trip[TripModel.id].value,
                refuelInputDto.driver,
                image.id
            )
        )
        val newFuelLevel = (getOne(refuelInputDto.car)?.fuelLevel!! + refuelInputDto.volume)
        update(
            refuelInputDto.car,
            CarUpdateDto(
                fuelLevel = newFuelLevel
            )
        )
    }
}