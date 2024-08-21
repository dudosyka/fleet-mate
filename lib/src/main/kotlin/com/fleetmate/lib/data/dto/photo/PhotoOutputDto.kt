package com.fleetmate.lib.data.dto.photo

import com.fleetmate.lib.conf.AppConf
import com.fleetmate.lib.data.model.photo.PhotoModel
import com.fleetmate.lib.exceptions.InternalServerException
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class PhotoOutputDto(
    val id: Int,
    val link: String,
    val date: Long,
    val type: AppConf.PhotoType
) {
    constructor(resultRow: ResultRow):
            this(
                resultRow[PhotoModel.id].value,
                resultRow[PhotoModel.link],
                resultRow[PhotoModel.createdAt].toEpochSecond(AppConf.defaultZoneOffset),
                AppConf.PhotoType.fromId(resultRow[PhotoModel.type]) ?: throw InternalServerException("Unknown photo type [id = ${resultRow[PhotoModel.id].value}]")
            )
}