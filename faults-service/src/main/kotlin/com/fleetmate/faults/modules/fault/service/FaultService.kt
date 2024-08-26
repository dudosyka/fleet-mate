package com.fleetmate.faults.modules.fault.service


import com.fleetmate.faults.modules.fault.data.dao.FaultDao
import com.fleetmate.faults.modules.fault.data.dto.FaultDto
import com.fleetmate.faults.modules.fault.data.dto.FaultInputDto
import com.fleetmate.lib.shared.modules.fault.model.FaultPhotoModel
import com.fleetmate.faults.modules.trip.TripDao
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.modules.auth.dto.AuthorizedUser
import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.car.model.part.CarPartModel
import com.fleetmate.lib.shared.modules.photo.service.PhotoService
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.lib.utils.kodein.KodeinService
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.instance

class FaultService(di: DI) : KodeinService(di) {
    private val photoService: PhotoService by instance()
    fun create(authorizedUser: AuthorizedUser, faultInputDto: FaultInputDto): FaultDto = transaction {
        val trip = try { TripDao.getCarActiveTrip(faultInputDto.carId) } catch (_: NotFoundException) {null}

        val fault = FaultDao.new {
            carId = EntityID(faultInputDto.carId, CarModel)
            carPartId = EntityID(faultInputDto.carPart, CarPartModel)
            tripId = trip?.id
            authorId = EntityID(authorizedUser.id, UserModel)
            comment = faultInputDto.comment
            critical = true
        }

        FaultPhotoModel.append(fault.idValue, photoService.upload(faultInputDto.photos.map { it.type = AppConf.PhotoType.FAULT; it }))

        fault.toOutputDto()
    }

    fun getByUser(authorizedUser: AuthorizedUser): List<FaultDto> {
        val trip = TripDao.getUserActiveTrip(authorizedUser.id)

        return FaultDao.getByCar(trip.carId.value)
    }

    fun getByCar(carId: Int): List<FaultDto> =
        FaultDao.getByCar(carId)
}