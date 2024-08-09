package com.fleetmate.trip.modules.violation.data.dto

import com.fleetmate.trip.modules.violation.data.model.ViolationModel
import org.jetbrains.exposed.sql.ResultRow
import kotlin.text.toFloat
import kotlin.text.toInt
import kotlin.text.toLong
import kotlin.toString

class ViolationOutputDto(
    val id: Int?,
    val date: Long?,
    val type: Int?,
    val duration: Float?,
    val hidden: Boolean?,
    val driver: Int?,
    val trip: Int?,
    val automobile: Int?,
    val comment: String?
) {
    constructor(resultRow: ResultRow):
            this(
                resultRow[ViolationModel.id].value,
                resultRow[ViolationModel.date].toString().toLong(),
                resultRow[ViolationModel.type].toString().toInt(),
                resultRow[ViolationModel.duration].toString().toFloat(),
                resultRow[ViolationModel.hidden],
                resultRow[ViolationModel.driver].value,
                resultRow[ViolationModel.trip].value,
                resultRow[ViolationModel.automobile].value,
                resultRow[ViolationModel.comment]
            )
}