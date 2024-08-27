package com.fleetmate.stat.modules.user.dto.output


import com.fleetmate.stat.modules.trip.dto.TripSimpleDto
import kotlinx.serialization.Serializable

@Serializable
data class DriverOutputDto (
    override val id: Int,
    override val fullName: String,
    val lastTrip: TripSimpleDto?,
    val licenceNumber: String,
    var violationCount: Long = 0L,
    override val photo: List<String>?,
): UserBaseOutput()
