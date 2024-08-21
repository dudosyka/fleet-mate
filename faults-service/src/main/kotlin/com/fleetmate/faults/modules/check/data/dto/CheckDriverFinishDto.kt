package com.fleetmate.faults.modules.check.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckDriverFinishDto(
    val checkId: Int,
    val photos: List<String>,
    val needWash: Boolean? = null,
    val mileage: Float
) {
}