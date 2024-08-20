package com.fleetmate.trip.modules.report.data.dto

import com.fleetmate.trip.modules.report.data.model.ReportModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
class ReportOutputDto (
    val id: Int?,
    val mileage: Float?,
    val avgSpeed: Float?,
    val trip: Int?,
    val car: Int?,
    val driver: Int?
){
    constructor(resultRow: ResultRow):
            this(
                resultRow[ReportModel.id].value,
                resultRow[ReportModel.mileage],
                resultRow[ReportModel.avgSpeed],
                resultRow[ReportModel.trip].value,
                resultRow[ReportModel.car].value,
                resultRow[ReportModel.driver].value
            )
}