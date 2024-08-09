package com.fleetmate.lib.dto.automobile

import kotlinx.serialization.Serializable

@Serializable
data class AutomobileTypeUpdateDto(
    val name: String?,
    val category: String?,
    val speedLimit: Float?,
    val speedError: Float?
)
