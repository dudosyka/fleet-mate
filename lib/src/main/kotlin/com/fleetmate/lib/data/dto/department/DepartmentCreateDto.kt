package com.fleetmate.lib.data.dto.department

import kotlinx.serialization.Serializable

@Serializable
data class DepartmentCreateDto(
    val name: String
)
