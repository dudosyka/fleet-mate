package com.fleetmate.stat.modules.order.data.dao


import com.fleetmate.lib.shared.modules.wash.model.WashModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.order.data.dto.WashDto
import com.fleetmate.stat.modules.trip.dao.TripDao
import com.fleetmate.stat.modules.user.dao.UserDao
import org.jetbrains.exposed.dao.id.EntityID

class WashDao(id: EntityID<Int>) : BaseIntEntity<WashDto>(id, WashModel) {
    companion object : BaseIntEntityClass<WashDto, WashDao>(WashModel)

    val tripId by WashModel.trip
    val trip by TripDao referencedOn WashModel.trip
    val authorId by WashModel.author
    val author by UserDao referencedOn WashModel.author

    override fun toOutputDto(): WashDto =
        WashDto(
            idValue, createdAt.toString(),
            trip.car.simpleDto, trip.simpleDto
        )
}