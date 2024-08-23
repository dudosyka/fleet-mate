package com.fleetmate.lib.shared.modules.photo.data.dto

import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.modules.photo.data.model.PhotoModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class PhotoOutputDto(
    val id: Int,
    val link: String,
    val date: Long,
    val type: String
) {
    constructor(resultRow: ResultRow):
            this(
                resultRow[PhotoModel.id].value,
                resultRow[PhotoModel.link],
                resultRow[PhotoModel.createdAt].toEpochSecond(AppConf.zoneOffset),
                resultRow[PhotoModel.type]
            )
}