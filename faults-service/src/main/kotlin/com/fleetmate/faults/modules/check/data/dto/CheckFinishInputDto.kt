package com.fleetmate.faults.modules.check.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckFinishInputDto(
    val checkId: Int,
    val photos: List<String>,
    val needWash: Boolean? = null
) {
}