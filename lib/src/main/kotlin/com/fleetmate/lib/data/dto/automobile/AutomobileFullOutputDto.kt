package com.fleetmate.lib.data.dto.automobile

import com.fleetmate.lib.dto.automobile.AutomobileOutputDto
import kotlinx.serialization.Serializable

@Serializable
class AutomobileFullOutputDto(
    val automobileOutputDto: AutomobileOutputDto,
    val automobileParts: List<AutomobilePartOutputDto>

) {

}