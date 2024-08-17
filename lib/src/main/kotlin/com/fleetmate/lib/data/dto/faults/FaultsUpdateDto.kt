package com.fleetmate.faults.modules.faults.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FaultsUpdateDto(
    val id: Int,
    val status: String?,
    val trip: Int?,
    val automobile: Int?,
    val photo: String?,
    val comment: String?,
    val critical: Boolean?
)
