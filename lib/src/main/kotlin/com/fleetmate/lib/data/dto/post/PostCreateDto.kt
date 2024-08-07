package com.fleetmate.lib.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class PostCreateDto(
    val name : String
) {
}