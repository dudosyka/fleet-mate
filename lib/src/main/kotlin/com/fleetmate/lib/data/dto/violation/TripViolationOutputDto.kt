package com.fleetmate.lib.data.dto.violation

import com.fleetmate.lib.model.automobile.AutomobileModel
import com.fleetmate.lib.model.trip.TripModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import kotlin.toString

@Serializable
class TripViolationOutputDto(
    val id: Int,
    val keyAcceptance: String,
    val route: String?,
    val avgSpeed: Float?,
    val keyReturn: String?,
    val automobileId: Int,
    val automobileMileage: Float,
    val automobileStateNumber: String,
    val automobileFuelLevel: Float,
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
        trip[AutomobileModel.id].value,
        trip[AutomobileModel.mileage],
        trip[AutomobileModel.stateNumber],
        trip[AutomobileModel.fuelLevel],
        violations
    )
}