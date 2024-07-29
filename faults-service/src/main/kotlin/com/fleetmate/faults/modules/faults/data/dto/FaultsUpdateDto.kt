package com.fleetmate.faults.modules.faults.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FaultsUpdateDto(
    val field: String
)
