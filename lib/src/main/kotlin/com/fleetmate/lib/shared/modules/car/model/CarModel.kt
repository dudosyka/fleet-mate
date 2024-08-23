package com.fleetmate.lib.shared.modules.car.model

import com.fleetmate.lib.shared.modules.car.model.type.CarTypeModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object CarModel: BaseIntIdTable() {
    val name = text("name")
    val registrationNumber = text("registration_number")
    val type = reference("type", CarTypeModel, ReferenceOption.RESTRICT, ReferenceOption.CASCADE)
    val fuelLevel = double("fuel_level")
    val mileage = double("mileage")
}