package com.fleetmate.lib.data.dto.car

import com.fleetmate.lib.data.model.car.CarTypeModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
class CarTypeOutputDto (
    val id: Int?,
    val name: String?,
    val category: String?,
    val speedLimit: Float?,
    val speedError: Float?,
    val avgFuelConsumption: Float?
){
    constructor(resultRow: ResultRow):
            this(
                resultRow[CarTypeModel.id].value,
                resultRow[CarTypeModel.name],
                resultRow[CarTypeModel.category],
                resultRow[CarTypeModel.speedLimit],
                resultRow[CarTypeModel.speedError],
                resultRow[CarTypeModel.avgFuelConsumption]
            )
}