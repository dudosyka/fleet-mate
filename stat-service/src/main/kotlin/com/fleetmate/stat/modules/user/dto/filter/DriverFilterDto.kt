package com.fleetmate.stat.modules.user.dto.filter


import kotlinx.serialization.Serializable

@Serializable
data class DriverFilterDto (
    val fullName: String? = null,
    val driverLicenceNumber: String? = null,
    val phoneNumber: String? = null
)
