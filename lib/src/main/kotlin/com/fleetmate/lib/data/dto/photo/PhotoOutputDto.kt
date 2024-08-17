package com.fleetmate.lib.dto.photo

import com.fleetmate.lib.model.photo.PhotoModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
class PhotoOutputDto(
    val id: Int,
    val link: String?,
    val date: String?,
    val type: String?
) {
    constructor(resultRow: ResultRow):
            this(
                resultRow[PhotoModel.id].value,
                resultRow[PhotoModel.link],
                resultRow[PhotoModel.date].toString(),
                resultRow[PhotoModel.type]
            )
}