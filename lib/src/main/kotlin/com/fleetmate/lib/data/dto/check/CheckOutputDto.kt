package com.fleetmate.lib.dto.check

import com.fleetmate.lib.dto.division.DivisionOutputDto
import com.fleetmate.lib.dto.post.PostOutputDto
import com.fleetmate.lib.dto.user.UserOutputDto
import com.fleetmate.lib.model.check.CheckModel
import com.fleetmate.lib.model.division.DivisionModel
import com.fleetmate.lib.model.post.PostModel
import com.fleetmate.lib.model.user.UserModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import kotlin.toString

@Serializable
class CheckOutputDto(
    val id: Int?,
    val author: UserOutputDto?,
    val startTime: String,
    val finishTime: String? = null,
    val timeExceeding: Boolean? = null
){
    constructor(resultRow: ResultRow):
            this(
                resultRow[CheckModel.id].value,
                UserOutputDto(
                    resultRow[UserModel.id].value,
                    resultRow[UserModel.fullName],
                    resultRow[UserModel.email],
                    resultRow[UserModel.phoneNumber],
                    PostOutputDto(
                        resultRow[PostModel.id].value,
                        resultRow[PostModel.name]
                    ),
                    DivisionOutputDto(
                        resultRow[DivisionModel.id].value,
                        resultRow[DivisionModel.name]
                    )
                ),
                resultRow[CheckModel.startTime].toString(),
                resultRow[CheckModel.finishTime].toString(),
                resultRow[CheckModel.timeExceeding]
            )
}