package com.fleetmate.stat.modules.violation.dao


import com.fleetmate.lib.shared.modules.violation.model.ViolationModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.car.dao.CarDao
import com.fleetmate.stat.modules.trip.dao.TripDao
import com.fleetmate.stat.modules.user.dao.UserDao
import com.fleetmate.stat.modules.violation.dto.ViolationDto
import com.fleetmate.stat.modules.trip.dto.TripViolationListItemDto
import org.jetbrains.exposed.dao.id.EntityID

class ViolationDao(id: EntityID<Int>) : BaseIntEntity<ViolationDto>(id, ViolationModel) {
    companion object : BaseIntEntityClass<ViolationDto, ViolationDao>(ViolationModel)

    var type by ViolationModel.type
    var registeredAt by ViolationModel.registeredAt
    var duration by ViolationModel.duration
    var hidden by ViolationModel.hidden
    var driverId by ViolationModel.driver
    val driver by UserDao referencedOn ViolationModel.driver
    var tripId by ViolationModel.trip
    val trip by TripDao referencedOn ViolationModel.trip
    var carId by ViolationModel.car
    var car by CarDao optionalReferencedOn ViolationModel.car
    var comment by ViolationModel.comment

    override fun toOutputDto(): ViolationDto =
        ViolationDto(
            idValue, type, duration, driverId.value,
            tripId.value, carId?.value, comment
        )

    val listTripDto: TripViolationListItemDto get() =
        TripViolationListItemDto(idValue, type, registeredAt, duration)
}