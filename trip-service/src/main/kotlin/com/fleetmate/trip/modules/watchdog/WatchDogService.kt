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

    fun emitPlaceholderOn(userId: Int) {
        webSocketRegister.emit {
            connections ->
                val connectionsByUser = connections[userId]
                (connectionsByUser ?: listOf()).forEach { connection ->
                    if (connection.isActive) {
                        connection.sendEncoded(WebSocketResponseDto.wrap("placeholder+on"))
                    }
                }
        }
    }

    fun emitPlaceholderOff(userId: Int) {
        webSocketRegister.emit {
            connections ->
                val connectionsByUser = connections[userId]
                (connectionsByUser ?: listOf()).forEach { connection ->
                    if (connection.isActive) {
                        connection.sendEncoded(WebSocketResponseDto.wrap("placeholder+off"))
                    }
                }
        }
    }

    fun emitSpeed(userId: Int, speed: Double) {
        webSocketRegister.emit {
            connections ->
                val connectionsByUser = connections[userId]
                (connectionsByUser ?: listOf()).forEach { connection ->
                    if (connection.isActive) {
                        connection.sendEncoded(WebSocketResponseDto.wrap("speed+$speed"))
                    }
                }
        }
    }

    fun emitViolation(userId: Int, violationAmount: Int) {
        webSocketRegister.emit {
                connections ->
            val connectionsByUser = connections[userId]
            (connectionsByUser ?: listOf()).forEach { connection ->
                if (connection.isActive) {
                    connection.sendEncoded(WebSocketResponseDto.wrap("violation+$violationAmount"))
                }
            }
        }
    }
}