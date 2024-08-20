package com.fleetmate.lib.data.dto.car

import com.fleetmate.lib.data.dto.faults.FaultDto
import kotlinx.serialization.Serializable

@Serializable
class CarPartOutputDto(
    val id: Int?,
    val name: String?,
    var children: List<CarPartOutputDto> = listOf(),
    var fault: FaultDto? = null
) {
}