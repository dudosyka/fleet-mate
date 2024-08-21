package com.fleetmate.trip.modules.refuel.service

import com.fleetmate.lib.conf.AppConf
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.car.data.dto.CarRefuelDto
import com.fleetmate.trip.modules.car.service.CarService
import com.fleetmate.lib.utils.files.PhotoService
import com.fleetmate.lib.data.dto.photo.PhotoUploadDto
import com.fleetmate.trip.modules.refuel.data.dto.RefuelCreateDto
import com.fleetmate.trip.modules.refuel.data.dto.RefuelInputDto
import com.fleetmate.trip.modules.refuel.data.dto.RefuelOutputDto
import com.fleetmate.trip.modules.refuel.data.model.RefuelModel
import com.fleetmate.trip.modules.trip.service.TripService
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.instance

class RefuelService(di: DI) : KodeinService(di) {
    private val carService: CarService by instance()
    private val photoService: PhotoService by instance()
    private val tripService: TripService by instance()
    fun checkRefuelNeed(carId: Int): CarRefuelDto {
        val car = carService.getOne(carId)

        return CarRefuelDto((car.fuelLevel) <= 10.0)
    }

    fun refuelCar(refuelInputDto: RefuelInputDto): RefuelOutputDto = transaction {
        val billPhoto = photoService.upload(
            PhotoUploadDto(
                refuelInputDto.billPhotoName,
                refuelInputDto.billPhoto,
                AppConf.PhotoType.REFUEL
            )
        )

        val trip = tripService.getActiveTrip(refuelInputDto.car)

        carService.refuel(trip.car, refuelInputDto.volume)

        RefuelOutputDto(RefuelModel.create(RefuelCreateDto(
            volume = refuelInputDto.volume,
            carId = trip.car,
            tripId = trip.id,
            driverId = trip.driver,
            billPhoto = billPhoto.id
        )))
    }
}