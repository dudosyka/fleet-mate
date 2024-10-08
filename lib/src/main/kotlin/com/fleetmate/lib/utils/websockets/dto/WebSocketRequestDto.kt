package com.fleetmate.lib.utils.websockets.dto

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketRequestDto (
    val headers: WebSocketRequestHeadersDto,
    val body: String
)
