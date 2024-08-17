package com.fleetmate.faults.modules.check.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckStartInputDto(
    val author: Int,
    val automobileId: Int
) {
}