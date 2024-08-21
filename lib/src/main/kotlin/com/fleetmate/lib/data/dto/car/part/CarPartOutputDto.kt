package com.fleetmate.lib.data.dto.car.part

import com.fleetmate.lib.data.dto.faults.FaultDto
import kotlinx.serialization.Serializable

@Serializable
data class CarPartOutputDto(
    val id: Int,
    val name: String,
    var children: List<CarPartOutputDto> = listOf(),
    var faults: List<FaultDto> = listOf()
)