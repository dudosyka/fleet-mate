package com.fleetmate.lib.data.dto.check

import com.fleetmate.lib.data.dto.user.UserOutputDto
import com.fleetmate.lib.data.model.check.CheckModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import kotlin.toString

@Serializable
data class CheckOutputDto(
    val id: Int,
    val author: UserOutputDto,
    val startTime: Long,
    val finishTime: Long? = null,
    val timeExceeded: Boolean? = null
){
    constructor(resultRow: ResultRow):
            this(
                resultRow[CheckModel.id].value,
                UserOutputDto(
                    resultRow
                ),
                resultRow[CheckModel.startTime].toString().toLong(),
                resultRow[CheckModel.finishTime].toString().toLong(),
                resultRow[CheckModel.timeExceeded]
            )

    companion object {
        fun constructFromNull(resultRow: ResultRow?): CheckOutputDto? =
            if (resultRow == null) null else CheckOutputDto(resultRow)
    }
}