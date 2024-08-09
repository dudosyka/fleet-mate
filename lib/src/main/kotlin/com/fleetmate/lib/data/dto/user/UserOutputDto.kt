package com.fleetmate.lib.dto.user

import com.fleetmate.lib.dto.division.DivisionOutputDto
import com.fleetmate.lib.dto.post.PostOutputDto
import com.fleetmate.lib.model.division.DivisionModel
import com.fleetmate.lib.model.post.PostModel
import com.fleetmate.lib.model.user.UserModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
class UserOutputDto (
    val id: Int?,
    val fullName: String?,
    val email: String?,
    val phoneNumber: String?,
    val post: PostOutputDto?,
    val division: DivisionOutputDto?
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
                PostOutputDto(
                    resultRow[UserModel.post].value,
                    resultRow[PostModel.name]
                ),
                DivisionOutputDto(
                    resultRow[DivisionModel.id].value,
                    resultRow[DivisionModel.name]
                )
            )
}