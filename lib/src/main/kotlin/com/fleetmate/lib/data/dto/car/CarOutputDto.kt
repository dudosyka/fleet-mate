package com.fleetmate.lib.data.dto.car

import com.fleetmate.lib.data.dto.car.type.CarTypeOutputDto
import com.fleetmate.lib.data.model.car.CarModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
@Serializable
data class CarOutputDto (
    val id: Int,
    val registrationNumber: String,
    val fuelLevel: Double,
    val mileage: Double,
    val dateAdded: String,
    val type: CarTypeOutputDto
)
{
    constructor(resultRow: ResultRow):
            this(
                resultRow[CarModel.id].value,
                resultRow[CarModel.registrationNumber],
                resultRow[CarModel.fuelLevel],
                resultRow[CarModel.mileage],
                resultRow[CarModel.createdAt].toString(),
                CarTypeOutputDto(
                    resultRow
                )
            )
}
