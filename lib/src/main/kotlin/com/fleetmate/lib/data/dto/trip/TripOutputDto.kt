package com.fleetmate.lib.dto.trip

import com.fleetmate.lib.model.trip.TripModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import kotlin.toString


@Serializable
class TripOutputDto (
    val id: Int?,
    val keyAcceptance: String?,
    val status: String?,
    val mechanicCheckBeforeTrip: Int?,
    val driverCheckBeforeTrip: Int?,
    val mechanicCheckAfterTrip: Int? = null,
    val driverCheckAfterTrip: Int? = null,
    val keyReturn: String? = null,
    val route: String? = null,
    val speedInfo: List<Float>? = null,
    val avgSpeed: Float? = null,
    val driver: Int?,
    val automobile: Int?,
    val questionable: Boolean? = null,
    val needWashing: Boolean? = null,
    val washHappen: Boolean? = null
) {
    constructor(resultRow: ResultRow?):
        this(
            resultRow?.get(TripModel.id)?.value,
            resultRow?.get(TripModel.keyAcceptance).toString(),
            resultRow?.get(TripModel.status),
            resultRow?.get(TripModel.mechanicCheckBeforeTrip)?.value,
            resultRow?.get(TripModel.driverCheckBeforeTrip)?.value,
            resultRow?.get(TripModel.mechanicCheckAfterTrip)?.value,
            resultRow?.get(TripModel.driverCheckAfterTrip)?.value,
            resultRow?.get(TripModel.keyReturn).toString(),
            resultRow?.get(TripModel.route),
            resultRow?.get(TripModel.speedInfo),
            resultRow?.get(TripModel.avgSpeed),
            resultRow?.get(TripModel.driver)?.value,
            resultRow?.get(TripModel.automobile)?.value,
            resultRow?.get(TripModel.questionable),
            resultRow?.get(TripModel.needWashing),
            resultRow?.get(TripModel.washHappen)
        )
}
