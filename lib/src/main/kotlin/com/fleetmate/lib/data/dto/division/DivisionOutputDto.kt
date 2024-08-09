package com.fleetmate.lib.dto.division

import com.fleetmate.lib.model.division.DivisionModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
class DivisionOutputDto (
    val id: Int?,
    val name: String?
){
    constructor(resultRow: ResultRow):
            this(
                resultRow[DivisionModel.id].value,
                resultRow[DivisionModel.name]
            )
}
