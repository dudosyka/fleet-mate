package com.fleetmate.lib.data.dto.department

import com.fleetmate.lib.data.model.department.DepartmentModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class DepartmentOutputDto (
    val id: Int,
    val name: String
){
    constructor(resultRow: ResultRow):
            this(
                resultRow[DepartmentModel.id].value,
                resultRow[DepartmentModel.name]
            )
}
