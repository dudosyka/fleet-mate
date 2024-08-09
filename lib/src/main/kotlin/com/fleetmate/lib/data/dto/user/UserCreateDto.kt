package com.fleetmate.lib.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UserCreateDto(
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val post: Int,
    val division: Int
)
