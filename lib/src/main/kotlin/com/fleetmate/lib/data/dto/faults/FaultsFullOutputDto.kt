package com.fleetmate.lib.data.dto.faults

import com.fleetmate.lib.data.model.faults.FaultsModel
import com.fleetmate.lib.data.dto.car.CarOutputDto
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class FaultsFullOutputDto (
    val id: Int,
    val createdAt: String,
    val status: String,
    val trip: Int?,
    val user: Int,
    val car: CarOutputDto,
    val comment: String?,
    val critical: Boolean,
    var photos: List<String> = listOf()
) {
    constructor(resultRow: ResultRow):
            this(
                resultRow[FaultsModel.id].value,
                resultRow[FaultsModel.createdAt].toString(),
                resultRow[FaultsModel.status],
                resultRow[FaultsModel.trip]?.value,
                resultRow[FaultsModel.user].value,
                CarOutputDto(
                    resultRow
                ),
                resultRow[FaultsModel.comment],
                resultRow[FaultsModel.critical]
            )
}