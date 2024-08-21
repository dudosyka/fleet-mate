package com.fleetmate.lib.data.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UserUpdateDto(
    val fullName: String?,
    val email: String?,
    val phoneNumber: String?,
    val position: Int?,
    val department: Int?,
    val birthday: Long?
)
