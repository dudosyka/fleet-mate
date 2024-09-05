package com.fleetmate.lib.shared.modules.car.model.type


import com.fleetmate.lib.shared.modules.car.model.licence.LicenceTypeModel
import com.fleetmate.lib.shared.modules.car.model.part.CarPartModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object CarTypeModel : BaseIntIdTable() {
    val name = text("name")
    val rootPart = reference("root_part", CarPartModel, ReferenceOption.SET_NULL, ReferenceOption.CASCADE).nullable().default(null)
    val licenceType = reference("licence_type", LicenceTypeModel, ReferenceOption.RESTRICT, ReferenceOption.CASCADE)
    val speedLimit = double("speed_limit")
    val speedError = double("speed_error")
    val avgFuelConsumption = double("avg_fuel_consumption")
    val photo = text("photo")
}