package com.fleetmate.trip.modules.refuel.data.dto

import com.fleetmate.lib.conf.AppConf
import com.fleetmate.trip.modules.refuel.data.model.RefuelModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class RefuelOutputDto (
    val id: Int,
    val createdAt: Long,
    val volume: Double,
    val carId: Int,
    val tripId: Int,
    val driverId: Int,
    val billPhoto: Int
){
    constructor(resultRow: ResultRow):
            this(
                resultRow[RefuelModel.id].value,
                resultRow[RefuelModel.createdAt].toEpochSecond(AppConf.defaultZoneOffset),
                resultRow[RefuelModel.volume],
                resultRow[RefuelModel.car].value,
                resultRow[RefuelModel.trip].value,
                resultRow[RefuelModel.driver].value,
                resultRow[RefuelModel.billPhoto].value
            )
}