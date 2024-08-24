package com.fleetmate.stat.modules.user.dto


import kotlinx.serialization.Serializable

@Serializable
data class LicenceTypeDto (
    val id: Int,
    val name: String
)
