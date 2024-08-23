package com.fleetmate.trip.modules.car.data.dto

import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.car.model.type.CarTypeModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class CarDto(
    val id: Int,
    val licenceType: Int,
    val fuelLevel: Double,
    val mileage: Double
) {
    constructor(resultRow: ResultRow): this(
        resultRow[CarModel.id].value,
        resultRow[CarTypeModel.licenceType].value,
        resultRow[CarModel.fuelLevel],
        resultRow[CarModel.mileage]
    )
}
