package com.fleetmate.faults.modules.trip


import com.fleetmate.faults.modules.check.dao.CheckDao
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.modules.car.model.CarPhotoModelModel
import com.fleetmate.lib.shared.modules.photo.data.dto.PhotoOutputDto
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import org.jetbrains.exposed.dao.id.EntityID

class TripDao(id: EntityID<Int>) : BaseIntEntity<TripDto>(id, TripModel) {
    companion object : BaseIntEntityClass<TripDto, TripDao>(TripModel) {
        fun getCarActiveTrip(carId: Int) =
            wrapRow(
                TripModel.getCarActiveTrip(carId) ?: throw NotFoundException("Active trip not found")
            )

        fun getUserActiveTrip(driverId: Int) =
            wrapRow(
                TripModel.getUserActiveTrip(driverId) ?: throw NotFoundException("Active trip not found")
            )
    }

    val carId by TripModel.car
    val driverId by TripModel.driver
    var status by TripModel.status
    var needWashing by TripModel.needWashing
    var mileage by TripModel.mileage
    var driverCheckBeforeTrip by CheckDao optionalReferencedOn TripModel.driverCheckBeforeTrip
    var driverCheckAfterTrip by CheckDao optionalReferencedOn TripModel.driverCheckAfterTrip
    var mechanicCheckBeforeTrip by CheckDao optionalReferencedOn TripModel.mechanicCheckBeforeTrip
    var mechanicCheckAfterTrip by CheckDao optionalReferencedOn TripModel.mechanicCheckAfterTrip

    val driverCheckBeforeTripCanBeFinished: Boolean get() =
        status == AppConf.TripStatus.INITIALIZED.name &&
        listOf(
            driverCheckBeforeTrip,
            mechanicCheckBeforeTrip,
            mechanicCheckAfterTrip,
            driverCheckAfterTrip
        ).all { it == null }

    val mechanicCheckBeforeTripCanBeFinished: Boolean get() =
        status == AppConf.TripStatus.INITIALIZED.name &&
        listOf(driverCheckBeforeTrip).all { it != null } &&
        listOf(
            mechanicCheckBeforeTrip,
            mechanicCheckAfterTrip,
            driverCheckAfterTrip
        ).all { it == null }

    val mechanicCheckAfterTripCanBeFinished: Boolean get() =
        status == AppConf.TripStatus.EXPLOITATION.name &&
        listOf(
            driverCheckBeforeTrip,
            mechanicCheckBeforeTrip
        ).all { it != null } &&
        listOf(
            mechanicCheckAfterTrip,
            driverCheckAfterTrip
        ).all { it == null }

    val driverCheckAfterTripCanBeFinished: Boolean get() =
        status == AppConf.TripStatus.EXPLOITATION.name &&
        listOf(
            driverCheckBeforeTrip,
            mechanicCheckBeforeTrip,
            mechanicCheckAfterTrip,
        ).all { it != null } &&
        listOf(
            driverCheckAfterTrip
        ).all { it == null }

    fun appendCarPhotos(checkId: Int?, photos: List<PhotoOutputDto>): Unit =
        CarPhotoModelModel.append(carId.value, checkId, photos)

    override fun toOutputDto(): TripDto =
        TripDto(
            idValue,
            carId.value,
            driverId.value,
            status,
            driverCheckBeforeTrip?.idValue,
            driverCheckAfterTrip?.idValue,
            mechanicCheckBeforeTrip?.idValue,
            mechanicCheckAfterTrip?.idValue,
        )
}