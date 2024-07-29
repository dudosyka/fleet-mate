package com.fleetmate.lib.utils.websockets.dto

import com.fleetmate.lib.dto.auth.AuthorizedUser
import com.fleetmate.lib.utils.websockets.ConnectionsRegister
import com.fleetmate.lib.utils.websockets.RoomsRegister
import io.ktor.websocket.*

data class RequestHandlerInput (
    val request: WebSocketRequestDto,
    val authorizedUser: AuthorizedUser,
    val socketSession: DefaultWebSocketSession,
    val actualConnections: ConnectionsRegister,
    val roomsRegister: RoomsRegister
)