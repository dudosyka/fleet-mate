package com.fleetmate.lib.data.dto.faults

import com.fleetmate.lib.data.model.faults.FaultsModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class FaultDto(
    val id: Int,
    val createdAt: String,
    val status: String,
    val trip: Int?,
    val user: Int,
    val car: Int,
    var photos: List<Int>?,
    val comment: String?,
    val critical: Boolean
) {
    constructor(resultRow: ResultRow, photos: List<Int> = listOf()):
            this(
                resultRow[FaultsModel.id].value,
                resultRow[FaultsModel.createdAt].toString(),
                resultRow[FaultsModel.status],
                resultRow[FaultsModel.trip]?.value,
                resultRow[FaultsModel.user].value,
                resultRow[FaultsModel.car].value,
                photos,
                resultRow[FaultsModel.comment],
                resultRow[FaultsModel.critical]
            )
}