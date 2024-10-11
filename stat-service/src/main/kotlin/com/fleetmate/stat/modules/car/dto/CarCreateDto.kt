package com.fleetmate.stat.modules.car.dto

import kotlinx.serialization.Serializable

@Serializable
data class CarCreateDto(
    val type: Int,
    val name: String,
    val mileage: Double,
    val fuelLevel: Double,
    val registrationNumber: String,
    val registrationCertificateNumber: String,
    val brand: String,
    val model: String,
    val vin: String,
    val fuelType: Int,
    val engineHours: Double,
    val compulsoryCarInsurance: String,
    val comprehensiveCarInsurance: String,
    val yearManufactured: Int,
    val lastMaintenance: Long,
    val antifreezeBrand: String,
    val engineOilBrand: String,
    val engineOilViscosity: String,
    val adBlue: Boolean,
    val ownership: Boolean
)