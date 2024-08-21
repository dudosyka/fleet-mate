package com.fleetmate.lib.data.dto.violation

import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.data.model.trip.TripModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import kotlin.toString

@Serializable
data class TripViolationOutputDto(
    val id: Int,
    val keyAcceptance: String,
    val route: String?,
    val avgSpeed: Double?,
    val keyReturn: String?,
    val carId: Int,
    val carMileage: Double,
    val carRegistrationNumber: String,
    val carFuelLevel: Double,
    val violations: List<ViolationOutputDto?>
) {
    constructor(
        trip: ResultRow,
        violations: List<ViolationOutputDto?>
    ): this(
        trip[TripModel.id].value,
        trip[TripModel.keyAcceptance].toString(),
        trip[TripModel.route],
        trip[TripModel.avgSpeed],
        trip[TripModel.keyReturn].toString(),
        trip[CarModel.id].value,
        trip[CarModel.mileage],
        trip[CarModel.registrationNumber],
        trip[CarModel.fuelLevel],
        violations
    )
}