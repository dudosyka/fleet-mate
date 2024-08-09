package com.fleetmate.trip.modules.refuel.data.dto

import com.fleetmate.trip.modules.refuel.data.model.RefuelModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import kotlin.text.toLong

@Serializable
class RefuelOutputDto (
    val id: Int?,
    val date: Long?,
    val volume: Float?,
    val automobile: Int?,
    val trip: Int?,
    val driver: Int?,
    val billPhoto: Int?
){
    constructor(resultRow: ResultRow):
            this(
                resultRow[RefuelModel.id].value,
                resultRow[RefuelModel.date].toString().toLong(),
                resultRow[RefuelModel.volume],
                resultRow[RefuelModel.automobile].value,
                resultRow[RefuelModel.trip].value,
                resultRow[RefuelModel.driver].value,
                resultRow[RefuelModel.billPhoto].value
            )
}