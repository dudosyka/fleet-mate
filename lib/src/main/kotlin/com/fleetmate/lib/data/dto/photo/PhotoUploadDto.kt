package com.fleetmate.lib.data.dto.photo

import com.fleetmate.lib.conf.AppConf
import kotlinx.serialization.Serializable

/**
 * DTO for uploading file FROM USER
 */
@Serializable
data class PhotoUploadDto (
    val photoName: String,
    val photo: String,
    val type: AppConf.PhotoType? = null
)