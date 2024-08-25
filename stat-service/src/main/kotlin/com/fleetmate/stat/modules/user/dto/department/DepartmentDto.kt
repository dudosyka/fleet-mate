package com.fleetmate.stat.modules.user.dto.department


import kotlinx.serialization.Serializable

@Serializable
data class DepartmentDto(
    val id: Int,
    val name: String
)
