package com.fleetmate.lib.utils.database

import kotlinx.serialization.Serializable

@Serializable
data class PaginationDto (
    val n: Int,
    val offset: Long,
)