package com.fleetmate.faults.modules.faults.data.dto

import com.fleetmate.faults.modules.faults.data.model.FaultsModel
import com.fleetmate.lib.dto.automobile.AutomobileOutputDto
import com.fleetmate.lib.dto.photo.PhotoOutputDto
import com.fleetmate.lib.model.automobile.AutomobileModel
import com.fleetmate.lib.model.photo.PhotoModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
class FaultsFullOutputDto (
    val id: Int,
    val date: String?,
    val status: String?,
    val trip: Int?,
    val user: Int,
    val automobile: AutomobileOutputDto?,
    val photo: PhotoOutputDto?,
    val comment: String?,
    val critical: Boolean
) {
    constructor(resultRow: ResultRow):
            this(
                resultRow[FaultsModel.id].value,
                resultRow[FaultsModel.date].toString(),
                resultRow[FaultsModel.status],
                resultRow[FaultsModel.trip]?.value,
                resultRow[FaultsModel.user].value,
                AutomobileOutputDto(
                    id = resultRow[AutomobileModel.id].value,
                    stateNumber = resultRow[AutomobileModel.stateNumber],
                    null,
                    null,
                    null,
                    null
                ),
                PhotoOutputDto(
                    id = resultRow[PhotoModel.id].value,
                    link = resultRow[PhotoModel.link],
                    date = null,
                    type = resultRow[PhotoModel.type]
                ),
                resultRow[FaultsModel.comment],
                resultRow[FaultsModel.critical]
                )
}