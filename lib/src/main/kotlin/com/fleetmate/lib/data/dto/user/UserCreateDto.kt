package com.fleetmate.lib.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UserCreateDto(
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val position: Int,
    val department: Int,
    val dateOfBirth: Long?
)
