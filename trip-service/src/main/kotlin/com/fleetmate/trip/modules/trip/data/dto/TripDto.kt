package com.fleetmate.trip.modules.trip.data.dto

import com.fleetmate.trip.modules.trip.data.model.TripModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
class TripDto (
    val id: Int,
    val field: String
) {
    constructor(resultRow: ResultRow): this(resultRow[TripModel.id].value, resultRow[TripModel.field])
}