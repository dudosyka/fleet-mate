package com.fleetmate.stat.modules.stat.data.dto

import com.fleetmate.stat.modules.stat.data.model.StatModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
class StatDto (
    val id: Int,
    val field: String
) {
    constructor(resultRow: ResultRow): this(resultRow[StatModel.id].value, resultRow[StatModel.field])
}