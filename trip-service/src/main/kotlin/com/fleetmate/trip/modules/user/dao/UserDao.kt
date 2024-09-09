package com.fleetmate.trip.modules.user.dao

import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.trip.modules.user.dto.UserDto
import org.jetbrains.exposed.dao.id.EntityID

class UserDao(id: EntityID<Int>) : BaseIntEntity<UserDto>(id, UserModel) {
    companion object: BaseIntEntityClass<UserDto, UserDao>(UserModel)

    val licenceNumber by UserModel.licenceNumber
    val fullName by UserModel.fullName
    val birthday by UserModel.birthday

    override fun toOutputDto(): UserDto =
        UserDto(
            idValue,
            licenceNumber ?: "",
            fullName,
            birthday
        )


}