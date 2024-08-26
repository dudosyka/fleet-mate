package com.fleetmate.stat.modules.user.dto.output

import kotlinx.serialization.Serializable

@Serializable
data class DriverDto(
    override val id: Int,
    override val fullName: String,
    val department: String,
    val snils: String,
    val phoneNumber: String,
    val licenceType: String,
    val sectorBossId: StaffDto,
    val position: String,
    override val photo: List<String>? = null
): UserBaseOutput()