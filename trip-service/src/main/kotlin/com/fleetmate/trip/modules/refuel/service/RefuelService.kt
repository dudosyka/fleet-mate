package com.fleetmate.trip.modules.refuel.service

import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.modules.photo.data.model.PhotoModel
import com.fleetmate.lib.shared.modules.photo.service.PhotoService
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.refuel.dao.RefuelDao
import com.fleetmate.trip.modules.refuel.dto.RefuelDto
import com.fleetmate.trip.modules.refuel.dto.RefuelInputDto
import com.fleetmate.trip.modules.trip.data.dao.TripDao
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.instance

class RefuelService(di: DI) : KodeinService(di) {
    private val photoService: PhotoService by instance()

    fun refuel(driverId: Int, refuelInputDto: RefuelInputDto): RefuelDto = transaction {
        val trip = TripDao.getUserActiveTrip(driverId)
        val photoDto = photoService.upload(refuelInputDto.billPhoto.apply { type = AppConf.PhotoType.REFUEL })

        trip.car.updateByRefuel(refuelInputDto)

        val refuelDao = RefuelDao.new {
            volume = refuelInputDto.volume
            tripId = EntityID(trip.idValue, TripModel)
            billPhoto = EntityID(photoDto.id, PhotoModel)
        }

        refuelDao.flush()

        refuelDao.toOutputDto()
    }
}