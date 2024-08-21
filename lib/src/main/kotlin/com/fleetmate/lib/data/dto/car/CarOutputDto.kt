package com.fleetmate.lib.dto.car

import com.fleetmate.lib.data.dto.car.CarTypeOutputDto
import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.data.model.car.CarTypeModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
@Serializable
class CarOutputDto (
    val id: Int?,
    val registrationNumber: String?,
    val fuelLevel: Float?,
    val mileage: Float?,
    val dateAdded: String?,
    val type: CarTypeOutputDto?
)
{
    constructor(resultRow: ResultRow):
            this(
                resultRow[CarModel.id].value,
                resultRow[CarModel.registrationNumber],
                resultRow[CarModel.fuelLevel],
                resultRow[CarModel.mileage],
                resultRow[CarModel.dateAdded].toString(),
                CarTypeOutputDto(
                    resultRow[CarTypeModel.id].value,
                    resultRow[CarTypeModel.name],
                    resultRow[CarTypeModel.category],
                    resultRow[CarTypeModel.speedLimit],
                    resultRow[CarTypeModel.speedError],
                    resultRow[CarTypeModel.avgFuelConsumption]
                )
            )
}
