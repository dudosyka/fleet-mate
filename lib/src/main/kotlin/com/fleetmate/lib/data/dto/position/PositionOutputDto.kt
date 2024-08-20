package com.fleetmate.lib.dto.post

import com.fleetmate.lib.model.post.PositionModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
class PositionOutputDto(
    val id: Int?,
    val name : String?
) {
    constructor(resultRow: ResultRow):
            this(
                resultRow[PositionModel.id].value,
                resultRow[PositionModel.name]
            )
}