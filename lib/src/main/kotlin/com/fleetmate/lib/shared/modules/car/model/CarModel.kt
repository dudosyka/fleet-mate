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
    val brand = text("brand")
    val model = text("model")
    val vin = text("vin")
    val engineHours = double("hours")
    val fuelType = text("fuel_type")
    val compulsoryCarInsurance = text("compulsory_car_insurance")
    val comprehensiveCarInsurance = text("comprehensive_car_insurance")
    val yearManufactured = integer("year_manufactured")
    val lastMaintenance = long("last_maintenance")
    val antifreezeBrand = text("antifreeze_brand")
    val engineOilBrand = text("engine_oil_brand")
    val engineOilViscosity = text("engine_oil_viscosity")
    val adBlue = bool("adBlue")
    val ownership = bool("ownership")

}