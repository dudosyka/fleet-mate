package com.fleetmate.faults.modules.user


import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import org.jetbrains.exposed.dao.id.EntityID

class UserDao(id: EntityID<Int>) : BaseIntEntity<UserDto>(id, UserModel) {
    companion object : BaseIntEntityClass<UserDto, UserDao>(UserModel)

    override fun toOutputDto(): UserDto =
        UserDto(idValue)
}