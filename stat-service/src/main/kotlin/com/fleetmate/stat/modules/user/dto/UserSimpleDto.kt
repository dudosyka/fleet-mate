package com.fleetmate.stat.modules.user.dto


import kotlinx.serialization.Serializable

@Serializable
class UserSimpleDto (
    val id: Int,
    val fullName: String,
)
