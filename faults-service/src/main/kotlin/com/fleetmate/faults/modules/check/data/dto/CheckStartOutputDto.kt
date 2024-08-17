package com.fleetmate.faults.modules.check.data.dto

import com.fleetmate.lib.model.check.CheckModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
class CheckStartOutputDto(
    val checkId: Int
) {
    constructor(resultRow: ResultRow):
            this(
                resultRow[CheckModel.id].value
            )
}