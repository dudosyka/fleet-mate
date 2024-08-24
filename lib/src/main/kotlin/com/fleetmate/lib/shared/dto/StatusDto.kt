package com.fleetmate.lib.shared.dto


import kotlinx.serialization.Serializable

@Serializable
data class StatusDto (
    val id: Int,
    val name: String
)
