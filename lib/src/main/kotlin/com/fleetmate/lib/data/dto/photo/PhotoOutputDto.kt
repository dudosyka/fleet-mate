package com.fleetmate.lib.dto.photo

import com.fleetmate.lib.model.photo.PhotoModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import kotlin.text.toLong

@Serializable
class PhotoOutputDto(
    val id: Int?,
    val link: String?,
    val date: Long?,
    val type: String?
) {
    constructor(resultRow: ResultRow):
            this(
                resultRow[PhotoModel.id].value,
                resultRow[PhotoModel.link],
                resultRow[PhotoModel.date].toString().toLong(),
                resultRow[PhotoModel.type]
            )
}