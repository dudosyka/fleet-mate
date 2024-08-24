package com.fleetmate.stat.modules.user.dto.output


import kotlinx.serialization.Serializable

@Serializable
data class StaffOutputDto(
    override val id: Int,
    override val fullName: String,
    val position: String,
    val orderInProgress: Long,
    val orderCompleted: Long,
    val hoursCompleted: Double,
): UserBaseOutput()
