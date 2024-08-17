package com.fleetmate.lib.dto.automobile

import com.fleetmate.lib.model.automobile.AutomobileTypeModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
class AutomobileTypeOutputDto (
    val id: Int?,
    val name: String?,
    val category: String?,
    val speedLimit: Float?,
    val speedError: Float?,
    val avgFuelConsumption: Float?
){
    constructor(resultRow: ResultRow):
            this(
                resultRow[AutomobileTypeModel.id].value,
                resultRow[AutomobileTypeModel.name],
                resultRow[AutomobileTypeModel.category],
                resultRow[AutomobileTypeModel.speedLimit],
                resultRow[AutomobileTypeModel.speedError],
                resultRow[AutomobileTypeModel.avgFuelConsumption]
            )
}