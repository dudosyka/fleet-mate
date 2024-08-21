package com.fleetmate.lib.dto.user

import com.fleetmate.lib.dto.division.DepartmentOutputDto
import com.fleetmate.lib.dto.post.PositionOutputDto
import com.fleetmate.lib.model.division.DepartmentModel
import com.fleetmate.lib.model.post.PositionModel
import com.fleetmate.lib.model.user.UserModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
class UserOutputDto (
    val id: Int?,
    val fullName: String?,
    val email: String?,
    val phoneNumber: String?,
    val position: PositionOutputDto?,
    val department: DepartmentOutputDto?
){
    constructor():
            this(
                null,
                null,
                null,
                null,
                null,
                null
            )

    constructor(resultRow: ResultRow):
            this(
                resultRow[UserModel.id].value,
                resultRow[UserModel.fullName],
                resultRow[UserModel.email],
                resultRow[UserModel.phoneNumber],
                PositionOutputDto(
                    resultRow[UserModel.position].value,
                    resultRow[PositionModel.name]
                ),
                DepartmentOutputDto(
                    resultRow[DepartmentModel.id].value,
                    resultRow[DepartmentModel.name]
                )
            )
}