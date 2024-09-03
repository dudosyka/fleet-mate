package com.fleetmate.trip.modules.wash.dao

import com.fleetmate.lib.shared.modules.wash.model.WashModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.trip.modules.trip.data.dao.TripDao
import com.fleetmate.trip.modules.user.dao.UserDao
import com.fleetmate.trip.modules.wash.dto.WashDto
import org.jetbrains.exposed.dao.id.EntityID

class WashDao(id: EntityID<Int>) : BaseIntEntity<WashDto>(id, WashModel) {
    companion object: BaseIntEntityClass<WashDto, WashDao>(WashModel)

    var tripId by WashModel.trip
    val trip by TripDao referencedOn WashModel.trip

    var authorId by WashModel.author
    val author by UserDao referencedOn WashModel.author
    var timestamp by WashModel.timestamp

    override fun toOutputDto(): WashDto =
        WashDto(
            idValue,
            tripId.value,
            authorId.value,
            timestamp
        )
}