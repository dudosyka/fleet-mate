package com.fleetmate.lib.data.dto.faults

import com.fleetmate.faults.modules.faults.data.model.FaultsModel
import com.fleetmate.lib.dto.photo.PhotoOutputDto
import com.fleetmate.lib.model.photo.PhotoModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
class FaultDto(
    val id: Int?,
    val photo: PhotoOutputDto?,
    val status: String?,
    val comment: String?,
    val critical: Boolean?
) {
    constructor(resultRow: ResultRow):
            this(
                resultRow[FaultsModel.id].value,
                PhotoOutputDto(
                    id = resultRow[PhotoModel.id].value,
                    link = resultRow[PhotoModel.link],
                    date = null,
                    type = resultRow[PhotoModel.type]
                ),
                resultRow[FaultsModel.status],
                resultRow[FaultsModel.comment],
                resultRow[FaultsModel.critical]
            )
}