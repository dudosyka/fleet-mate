package com.fleetmate.stat.modules.user.dao


import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.user.dto.UserDto
import org.jetbrains.exposed.dao.id.EntityID

class UserDao(id: EntityID<Int>) : BaseIntEntity<UserDto>(id, UserModel) {
    companion object : BaseIntEntityClass<UserDto, UserDao>(UserModel)

    val login by UserModel.login
    val email by UserModel.email
    val phoneNumber by UserModel.phoneNumber
    val hash by UserModel.hash
    val fullName by UserModel.fullName
    val birthday by UserModel.birthday
    val licenceType by UserModel.licenceType
    val positionId by UserModel.position
    val position by PositionDao referencedOn UserModel.position
    val departmentId by UserModel.department
    val department by DepartmentDao referencedOn UserModel.department

    override fun toOutputDto(): UserDto =
        UserDto(
            idValue, login, email, phoneNumber,
            fullName, birthday, licenceType.value,
            positionId.value, departmentId.value
        )

}