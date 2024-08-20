package com.fleetmate.trip.modules.watchdog

import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.lib.utils.websockets.WebSocketRegister
import com.fleetmate.lib.utils.websockets.dto.WebSocketResponseDto
import org.kodein.di.DI
import org.kodein.di.instance

class WatchDogService(di: DI) : KodeinService(di) {
    private val webSocketRegister: WebSocketRegister by instance()

    fun emitSpeeding(userId: Int) {
        webSocketRegister.emit {
            connections ->
                val connectionsByUser = connections[userId]
                (connectionsByUser ?: listOf()).forEach { connection ->
                    if (connection.isActive) {
                        connection.sendEncoded(WebSocketResponseDto.wrap("speeding"))
                    }
                }
        }
    }
}