package com.fleetmate.lib.shared.modules.photo.data.dto

import com.fleetmate.lib.shared.conf.AppConf
import kotlinx.serialization.Serializable


/**
 * DTO to provide data to database model
 */
@Serializable
data class PhotoCreateDto(
    val link: String,
    val type: AppConf.PhotoType,
    val original: String? = null,
) {
}