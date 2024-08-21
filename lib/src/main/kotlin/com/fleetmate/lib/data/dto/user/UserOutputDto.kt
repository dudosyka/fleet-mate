package com.fleetmate.lib.data.dto.user

import com.fleetmate.lib.data.dto.department.DepartmentOutputDto
import com.fleetmate.lib.data.dto.position.PositionOutputDto
import com.fleetmate.lib.data.model.user.UserModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class UserOutputDto (
    val id: Int,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val position: PositionOutputDto,
    val department: DepartmentOutputDto,
    val birthday: Long,
){
    constructor(resultRow: ResultRow):
            this(
                resultRow[UserModel.id].value,
                resultRow[UserModel.fullName],
                resultRow[UserModel.email],
                resultRow[UserModel.phoneNumber],
                PositionOutputDto(
                    resultRow
                ),
                DepartmentOutputDto(
                    resultRow
                ),
                resultRow[UserModel.birthday]
            )
}