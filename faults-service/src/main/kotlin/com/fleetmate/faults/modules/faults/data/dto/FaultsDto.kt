package com.fleetmate.faults.modules.faults.data.dto

import com.fleetmate.faults.modules.faults.data.model.FaultsModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
class FaultsDto (
    val id: Int,
    val field: String
) {
    constructor(resultRow: ResultRow): this(resultRow[FaultsModel.id].value, resultRow[FaultsModel.field])
}