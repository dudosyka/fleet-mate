package com.fleetmate.lib.dto.post

import com.fleetmate.lib.model.post.PostModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
class PostOutputDto(
    val id: Int?,
    val name : String?
) {
    constructor(resultRow: ResultRow):
            this(
                resultRow[PostModel.id].value,
                resultRow[PostModel.name]
            )
}