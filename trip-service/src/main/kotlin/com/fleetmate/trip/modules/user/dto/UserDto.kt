package com.fleetmate.trip.modules.user.dto

import com.fleetmate.lib.shared.modules.user.model.UserModel
import org.jetbrains.exposed.sql.ResultRow

data class UserDto(
    val id: Int,
    val licenceType: Int,
    val fullName: String,
    val birthday: Long
) {
    constructor(resultRow: ResultRow): this(
        resultRow[UserModel.id].value,
        resultRow[UserModel.licenceType].value,
        resultRow[UserModel.fullName],
        resultRow[UserModel.birthday]
    )
}
