package com.fleetmate.lib.shared.modules.photo.data.dto

import com.fleetmate.lib.shared.conf.AppConf
import kotlinx.serialization.Serializable

/**
 * DTO for uploading file FROM USER
 */
@Serializable
data class PhotoUploadDto (
    val photoName: String,
    val photo: String,
    var type: AppConf.PhotoType? = null
)