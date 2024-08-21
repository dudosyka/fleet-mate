package com.fleetmate.lib.data.dto.violation

import com.fleetmate.lib.data.model.violation.ViolationModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import kotlin.text.toDouble
import kotlin.text.toInt
import kotlin.toString

@Serializable
data class ViolationOutputDto(
    val id: Int?,
    val date: String?,
    val type: Int?,
    val duration: Double?,
    val hidden: Boolean?,
    val driver: Int?,
    val trip: Int?,
    val car: Int?,
    val comment: String?
) {
    constructor(resultRow: ResultRow):
            this(
                resultRow[ViolationModel.id].value,
                resultRow[ViolationModel.date].toString(),
                resultRow[ViolationModel.type].toString().toInt(),
                resultRow[ViolationModel.duration].toString().toDouble(),
                resultRow[ViolationModel.hidden],
                resultRow[ViolationModel.driver].value,
                resultRow[ViolationModel.trip].value,
                resultRow[ViolationModel.car].value,
                resultRow[ViolationModel.comment]
            )
}