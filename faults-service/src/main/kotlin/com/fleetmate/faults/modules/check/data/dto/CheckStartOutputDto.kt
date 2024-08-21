package com.fleetmate.faults.modules.check.data.dto

import com.fleetmate.lib.data.model.check.CheckModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class CheckStartOutputDto(
    val checkId: Int
) {
    constructor(resultRow: ResultRow):
            this(
                resultRow[CheckModel.id].value
            )
}