package com.fleetmate.stat.modules.car.dto

import com.fleetmate.lib.shared.conf.AppConf
import kotlinx.serialization.Serializable

@Serializable
data class CarCreateDto(
    val type: Int,
    val name: String,
    val mileage: Double,
    val fuelLevel: Double,
    val registerNumber: String,
    val brand: String,
    val model: String,
    val vin: String,
    val fuelType: AppConf.FuelType,
    val hours: Double,
    val osago: Long,
    val casco: Long,
    val yearManufacture: Int,
    val lastMaintenance: Long,
    val antifreezeBrand: String,
    val engineOilBrand: String,
    val engineOilViscosity: String,
    val adBlue: Boolean,
    val ownership: Boolean
)