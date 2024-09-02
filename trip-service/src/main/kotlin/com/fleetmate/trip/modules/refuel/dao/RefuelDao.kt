package com.fleetmate.trip.modules.refuel.dao

import com.fleetmate.lib.shared.modules.refuel.model.RefuelModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.trip.modules.refuel.dto.RefuelDto
import com.fleetmate.trip.modules.trip.data.dao.TripDao
import org.jetbrains.exposed.dao.id.EntityID

class RefuelDao(id: EntityID<Int>) : BaseIntEntity<RefuelDto>(id, RefuelModel) {
    companion object: BaseIntEntityClass<RefuelDto, RefuelDao>(RefuelModel)

    var volume by RefuelModel.volume
    var trip by TripDao referencedOn RefuelModel.trip
    var tripId by RefuelModel.trip
    var billPhoto by RefuelModel.billPhone

    override fun toOutputDto(): RefuelDto =
        RefuelDto(
            idValue,
            volume,
            tripId.value
        )
}