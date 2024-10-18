package com.fleetmate.stat.modules.user.dto.output

import kotlinx.serialization.Serializable

@Serializable
data class SimpleStaffDto(
    val id: Int,
    val fullName: String
)
