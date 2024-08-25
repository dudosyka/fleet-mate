package com.fleetmate.stat.modules.user.dto.driver


import kotlinx.serialization.Serializable

@Serializable
data class LicenceTypeDto (
    val id: Int,
    val name: String
)
