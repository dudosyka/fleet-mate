package com.fleetmate.lib.dto.photo

import kotlinx.serialization.Serializable

@Serializable
data class PhotoUpdateDto(
    val link: String? = null,
    val date: Long? = null,
    val type: String? = null
)