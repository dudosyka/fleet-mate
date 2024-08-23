package com.fleetmate.stat.modules.user.dto


import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val login: String,
    val email: String,
    val phoneNumber: String,
    val fullName: String,
    val birthday: Long,
    val licenceTypeId: Int,
    val positionId: Int,
    val departmentId: Int
)
