package com.fleetmate.lib.data.dto.photo

import com.fleetmate.lib.conf.AppConf
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