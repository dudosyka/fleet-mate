package com.fleetmate.lib.dto.photo

import kotlinx.serialization.Serializable


@Serializable
data class PhotoCreateDto(
    val link: String,
    val date: Long,
    val type: String
) {
}