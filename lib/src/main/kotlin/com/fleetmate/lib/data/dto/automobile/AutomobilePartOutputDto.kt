package com.fleetmate.lib.data.dto.automobile

import com.fleetmate.lib.data.dto.faults.FaultDto
import kotlinx.serialization.Serializable

@Serializable
class AutomobilePartOutputDto(
    val id: Int?,
    val name: String?,
    var children: List<AutomobilePartOutputDto> = listOf(),
    var fault: FaultDto? = null
) {
}