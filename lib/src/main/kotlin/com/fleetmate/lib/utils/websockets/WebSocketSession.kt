package com.fleetmate.lib.utils.websockets

import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import com.fleetmate.lib.utils.websockets.dto.WebSocketResponseDto

class WebSocketSession(
    private val defaultWebSocketSession: DefaultWebSocketSession,
) {
    val isActive: Boolean get() = defaultWebSocketSession.isActive
    suspend fun send(responseDto: WebSocketResponseDto) {
        defaultWebSocketSession.send(responseDto.json)
    }
    suspend fun close(reason: CloseReason.Codes, message: String) {
        defaultWebSocketSession.close(CloseReason(reason, message))
    }
}