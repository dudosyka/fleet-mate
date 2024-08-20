package com.fleetmate.lib.utils.websockets

import com.fleetmate.lib.utils.security.ecdh.AesUtil
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import com.fleetmate.lib.utils.websockets.dto.WebSocketResponseDto

class WebSocketSession(
    private val defaultWebSocketSession: DefaultWebSocketSession,
) {
    var ecdhSecret: String = ""
    val isActive: Boolean get() = defaultWebSocketSession.isActive
    suspend fun send(responseDto: WebSocketResponseDto) {
        defaultWebSocketSession.send(responseDto.json)
    }
    suspend fun sendEncoded(responseDto: WebSocketResponseDto) {
        val encoded = AesUtil.encrypt(ecdhSecret, responseDto.json.toByteArray())
        defaultWebSocketSession.send(encoded)
    }
    suspend fun close(reason: CloseReason.Codes, message: String) {
        defaultWebSocketSession.close(CloseReason(reason, message))
    }
}