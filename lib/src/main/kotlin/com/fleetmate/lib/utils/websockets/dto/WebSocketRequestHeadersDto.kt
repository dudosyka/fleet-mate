package com.fleetmate.lib.utils.websockets.dto

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketRequestHeadersDto (
    val authorization: String,
    val uri: String,
    val bridgeX: String = "",
    val bridgeY: String = "",
)