package com.fleetmate.stat.modules.fault.dto

import kotlinx.serialization.Serializable

@Serializable
data class FaultOutputDto(
    val id: Int,
    val createdAt: String,
    val authorName: String,
    val type: String,
    val status: String,
    val comment: String? = null,
    val photo: List<String>? = null
)