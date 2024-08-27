package com.fleetmate.faults.modules.fault.service


import com.fleetmate.faults.modules.car.CarDao
import com.fleetmate.faults.modules.car.CarPartDao
import com.fleetmate.faults.modules.fault.data.dao.FaultDao
import com.fleetmate.faults.modules.fault.data.dto.FaultDto
import com.fleetmate.faults.modules.fault.data.dto.FaultInputDto
import com.fleetmate.lib.shared.modules.fault.model.FaultPhotoModel
import com.fleetmate.faults.modules.trip.TripDao
import com.fleetmate.faults.modules.user.UserDao
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.modules.auth.dto.AuthorizedUser
import com.fleetmate.lib.shared.modules.photo.service.PhotoService
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.lib.utils.kodein.KodeinService
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.instance

class FaultService(di: DI) : KodeinService(di) {
    private val photoService: PhotoService by instance()
    fun create(authorizedUser: AuthorizedUser, faultInputDto: FaultInputDto): FaultDto = transaction {
        val trip = try { TripDao.getCarActiveTrip(faultInputDto.carId) } catch (_: NotFoundException) { null }

        val fault = FaultDao.new {
            car = CarDao[faultInputDto.carId]
            carPart = CarPartDao[faultInputDto.carPart]
            tripId = trip?.id
            author = UserDao[authorizedUser.id]
            comment = faultInputDto.comment
            critical = true
        }

        FaultPhotoModel.append(fault.idValue, photoService.upload(faultInputDto.photos.map { it.type = AppConf.PhotoType.FAULT; it }))

        fault.toOutputDto()
    }

    /**
     * Retrieves active trip by UserId and returns Faults for car from the trip
     */
    fun getByUser(authorizedUser: AuthorizedUser): List<FaultDto> = transaction {
        val trip = TripDao.getUserActiveTrip(authorizedUser.id)

        FaultDao.getByCar(trip.carId.value).map(FaultDao::toOutputDto)
    }

    fun getByCar(carId: Int): List<FaultDto> = transaction {
        FaultDao.getByCar(carId).map(FaultDao::toOutputDto)
    }

    fun getCriticalByCar(carId: Int): List<FaultDto> = transaction {
        FaultDao.getCriticalByCar(carId).map(FaultDao::toOutputDto)
    }
}