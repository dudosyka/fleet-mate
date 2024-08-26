package com.fleetmate.stat.modules.user.dto.output

import kotlinx.serialization.Serializable

@Serializable
data class StaffDto (
    override val id: Int,
    override val fullName: String,
    val phoneNumber: String,
    val department: String,
    val position: String,
    override val photo: List<String>? = null
): UserBaseOutput()