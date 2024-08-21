package com.fleetmate.lib.dto.division

import com.fleetmate.lib.model.division.DepartmentModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
class DepartmentOutputDto (
    val id: Int?,
    val name: String?
){
    constructor(resultRow: ResultRow):
            this(
                resultRow[DepartmentModel.id].value,
                resultRow[DepartmentModel.name]
            )
}
