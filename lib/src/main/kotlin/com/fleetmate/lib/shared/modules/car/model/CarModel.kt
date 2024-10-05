package com.fleetmate.lib.shared.modules.car.model

import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.modules.car.model.type.CarTypeModel
import com.fleetmate.lib.shared.modules.type.model.FuelTypeModel
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
    val registrationCertificateNumber = text("registration_certificate_number")
    val engineHours = double("hours")
    val fuelType = reference("fuel_type", FuelTypeModel, ReferenceOption.RESTRICT, ReferenceOption.CASCADE)
    val compulsoryCarInsurance = text("compulsory_car_insurance")
    val comprehensiveCarInsurance = text("comprehensive_car_insurance")
    val yearManufactured = integer("year_manufactured")
    val lastMaintenance = long("last_maintenance")
    val antifreezeBrand = text("antifreeze_brand")
    val engineOilBrand = text("engine_oil_brand")
    val engineOilViscosity = text("engine_oil_viscosity")
    val adBlue = bool("adBlue")
    val ownership = bool("ownership")
    val status = text("status").default(AppConf.CarStatus.FREE.name)

}