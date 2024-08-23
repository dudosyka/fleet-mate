package com.fleetmate.stat.modules.fault.dao


import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.car.dao.CarDao
import com.fleetmate.stat.modules.car.dao.CarPartDao
import com.fleetmate.stat.modules.fault.dto.FaultDto
import com.fleetmate.stat.modules.trip.dao.TripDao
import com.fleetmate.stat.modules.user.dao.UserDao
import org.jetbrains.exposed.dao.id.EntityID

class FaultDao(id: EntityID<Int>) : BaseIntEntity<FaultDto>(id, FaultModel) {
    companion object : BaseIntEntityClass<FaultDto, FaultDao>(FaultModel)

    val carId by FaultModel.car
    val car by CarDao referencedOn FaultModel.car
    val carPartId by FaultModel.carPart
    val carPart by CarPartDao referencedOn FaultModel.carPart
    val tripId by FaultModel.trip
    val trip by TripDao optionalReferencedOn FaultModel.trip
    val authorId by FaultModel.author
    val author by UserDao referencedOn FaultModel.author
    val comment by FaultModel.comment
    val critical by FaultModel.critical

    override fun toOutputDto(): FaultDto =
        FaultDto(
            idValue, carId.value, carPartId.value,
            tripId?.value, authorId.value, comment, critical
        )

}