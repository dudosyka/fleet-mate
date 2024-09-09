package com.fleetmate.trip.modules.user.dto

import com.fleetmate.lib.shared.modules.user.model.UserModel
import org.jetbrains.exposed.sql.ResultRow

data class UserDto(
    val id: Int,
    val licenceNumber: String,
    val fullName: String,
    val birthday: Long,
    var licenceTypes: List<Int> = listOf(),
) {
    constructor(resultRow: ResultRow): this(
        resultRow[UserModel.id].value,
        resultRow[UserModel.licenceNumber] ?: "",
        resultRow[UserModel.fullName],
        resultRow[UserModel.birthday]
    )
}
