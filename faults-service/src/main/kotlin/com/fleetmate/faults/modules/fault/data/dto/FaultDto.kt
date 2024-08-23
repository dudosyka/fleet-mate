package com.fleetmate.faults.modules.fault.data.dto


import com.fleetmate.lib.shared.modules.photo.data.dto.PhotoOutputDto
import kotlinx.serialization.Serializable

@Serializable
data class FaultDto (
    val id: Int,
    val carId: Int,
    val carPartId: Int,
    val tripId: Int?,
    val authorId: Int,
    val comment: String,
    val critical: Boolean,
    val photos: List<PhotoOutputDto> = listOf()
)
