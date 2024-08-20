package com.fleetmate.lib.data.dto.car

import com.fleetmate.lib.dto.car.CarOutputDto
import kotlinx.serialization.Serializable

@Serializable
class CarFullOutputDto(
    val carOutputDto: CarOutputDto,
    val carParts: List<CarPartOutputDto>

) {

}