package com.fleetmate.lib.dto.automobile

import com.fleetmate.lib.model.automobile.AutomobileModel
import com.fleetmate.lib.model.automobile.AutomobileTypeModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
@Serializable
class AutomobileOutputDto (
    val id: Int?,
    val stateNumber: String?,
    val fuelLevel: Float?,
    val mileage: Float?,
    val additionalDate: String?,
    val type: AutomobileTypeOutputDto?
)
{
    constructor(resultRow: ResultRow):
            this(
                resultRow[AutomobileModel.id].value,
                resultRow[AutomobileModel.stateNumber],
                resultRow[AutomobileModel.fuelLevel],
                resultRow[AutomobileModel.mileage],
                resultRow[AutomobileModel.additionDate].toString(),
                AutomobileTypeOutputDto(
                    resultRow[AutomobileTypeModel.id].value,
                    resultRow[AutomobileTypeModel.name],
                    resultRow[AutomobileTypeModel.category],
                    resultRow[AutomobileTypeModel.speedLimit],
                    resultRow[AutomobileTypeModel.speedError]
                )
            )
}
