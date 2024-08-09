package com.fleetmate.lib.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UserUpdateDto(
    val fullName: String?,
    val email: String?,
    val phoneNumber: String?,
    val post: Int?,
    val division: Int?
)
