package com.fleetmate.lib.data.dto.trip

import com.fleetmate.lib.data.model.trip.TripModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import kotlin.toString


@Serializable
data class TripOutputDto(
    val id: Int,
    val keyAcceptance: String,
    val status: String,
    val mechanicCheckBeforeTrip: Int? = null,
    val driverCheckBeforeTrip: Int? = null,
    val mechanicCheckAfterTrip: Int? = null,
    val driverCheckAfterTrip: Int? = null,
    val keyReturn: String? = null,
    val route: String? = null,
    val speedInfo: List<Double>,
    val avgSpeed: Double? = null,
    val driver: Int,
    val car: Int,
    val questionable: Boolean? = null,
    val needWashing: Boolean? = null,
    val washHappen: Boolean? = null
) {
    constructor(resultRow: ResultRow):
        this(
            resultRow[TripModel.id].value,
            resultRow[TripModel.keyAcceptance].toString(),
            resultRow[TripModel.status],
            resultRow[TripModel.mechanicCheckBeforeTrip]?.value,
            resultRow[TripModel.driverCheckBeforeTrip]?.value,
            resultRow[TripModel.mechanicCheckAfterTrip]?.value,
            resultRow[TripModel.driverCheckAfterTrip]?.value,
            resultRow[TripModel.keyReturn].toString(),
            resultRow[TripModel.route],
            resultRow[TripModel.speedInfo],
            resultRow[TripModel.avgSpeed],
            resultRow[TripModel.driver].value,
            resultRow[TripModel.car].value,
            resultRow[TripModel.questionable],
            resultRow[TripModel.needWashing],
            resultRow[TripModel.washHappen]
        )
}
