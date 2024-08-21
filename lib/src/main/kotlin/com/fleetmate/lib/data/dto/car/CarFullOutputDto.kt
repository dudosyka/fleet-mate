package com.fleetmate.lib.data.dto.car

import com.fleetmate.lib.data.dto.car.part.CarPartOutputDto
import kotlinx.serialization.Serializable

@Serializable
data class CarFullOutputDto(
    val carOutputDto: CarOutputDto,
    val carParts: List<CarPartOutputDto>
) {

}