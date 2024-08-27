package com.fleetmate.stat.modules.car.dto

import kotlinx.serialization.Serializable

@Serializable
data class CarOutputDto(
    val id: Int,
    val brand: String,
    val model: String,
    val registrationModel: String,
    val vin: String,
    val fuelType: String,
    val mileage: Double,
    val hours: Double,
    val type: Int,
    val licenceType: Int,
    val compulsoryCarInsurance: String,
    val comprehensiveCarInsurance: String,
    val yearManufactured: Int,
    val serviceability: Boolean,
    val lastMaintenance: Long,
    val antifreezeBrand: String,
    val engineOilBrand: String,
    val engineOilViscosity: String,
    val adBlue: Boolean,
    val ownership: Boolean,
    val photos: List<String>
)