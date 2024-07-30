package com.fleetmate.lib.utils.security.ecdh

import kotlinx.serialization.Serializable

@Serializable
data class PointDto(
    var x: String = "",
    var y: String = ""
) {
    fun createKeyBase(): String = "$x$y"
}
