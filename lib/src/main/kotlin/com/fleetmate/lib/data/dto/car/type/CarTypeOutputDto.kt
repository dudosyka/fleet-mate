package com.fleetmate.lib.data.dto.car.type

import com.fleetmate.lib.data.model.car.CarTypeModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class CarTypeOutputDto (
    val id: Int,
    val name: String,
    val category: String,
    val speedLimit: Double,
    val speedError: Double,
    val avgFuelConsumption: Double
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