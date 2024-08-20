package com.fleetmate.lib.data.dto.car

import com.fleetmate.lib.dto.division.DepartmentOutputDto
import com.fleetmate.lib.dto.post.PositionOutputDto
import com.fleetmate.lib.dto.user.UserOutputDto
import com.fleetmate.lib.model.check.CheckModel
import com.fleetmate.lib.model.division.DepartmentModel
import com.fleetmate.lib.model.post.PositionModel
import com.fleetmate.lib.model.user.UserModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import kotlin.toString

@Serializable
class CheckOutputDto(
    val id: Int?,
    val author: UserOutputDto?,
    val startTime: Long,
    val finishTime: Long? = null,
    val timeExceeded: Boolean? = null
){
    constructor(resultRow: ResultRow):
            this(
                resultRow[CheckModel.id].value,
                UserOutputDto(
                    resultRow[UserModel.id].value,
                    resultRow[UserModel.fullName],
                    resultRow[UserModel.email],
                    resultRow[UserModel.phoneNumber],
                    PositionOutputDto(
                        resultRow[PositionModel.id].value,
                        resultRow[PositionModel.name]
                    ),
                    DepartmentOutputDto(
                        resultRow[DepartmentModel.id].value,
                        resultRow[DepartmentModel.name]
                    )
                ),
                resultRow[CheckModel.startTime].toString().toLong(),
                resultRow[CheckModel.finishTime].toString().toLong(),
                resultRow[CheckModel.timeExceeded]
            )
}