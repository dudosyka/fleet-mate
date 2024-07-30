package com.fleetmate.lib.plugins

import com.fleetmate.lib.conf.AppConf
import com.fleetmate.lib.exceptions.BadRequestException
import com.fleetmate.lib.utils.security.ecdh.AesUtil
import com.fleetmate.lib.utils.security.ecdh.PointDto
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.utils.io.*
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

val aesCryptPluginRespond = createApplicationPlugin(name = "AesCryptPluginRespond") {
    onCallRespond {
        call -> if (AppConf.needCrypt) {
            val publicX = call.request.headers["bridgeX"] ?: throw BadRequestException("Bad headers")
            val publicY = call.request.headers["bridgeY"] ?: throw BadRequestException("Bad headers")
            transformBody { data ->
                try {
                    data as TextContent
                    Logger.debug("ON RESPOND: ")
                    Logger.debug(data.text)
                    Logger.debug(data.javaClass)
                    val bytes = data.text.toByteArray(Charsets.UTF_8)
                    AesUtil.encrypt(bytes, PointDto(publicX, publicY))
                } catch (_: Exception) {
                    data
                }
            }
        }
    }
}

@OptIn(InternalSerializationApi::class)
val aesCryptPluginReceive = createApplicationPlugin(name = "AesCryptPluginReceive") {
    onCallReceive {
        call -> if (AppConf.needCrypt) {
            val publicX = call.request.headers["bridgeX"] ?: throw BadRequestException("Bad headers")
            val publicY = call.request.headers["bridgeY"] ?: throw BadRequestException("Bad headers")

            transformBody { data ->
                val bytes = ByteArray(data.availableForRead)
                data.readFully(bytes)
                try {
                    Json { ignoreUnknownKeys = true }.decodeFromString(call.receiveType.type.serializer(), String(AesUtil.decrypt(bytes, PointDto(publicX, publicY))))
                } catch (e: SerializationException) {
                    throw BadRequestException("Bad format")
                } catch (e: IllegalArgumentException) {
                    throw BadRequestException("Bad format")
                }
            }
        }
    }
}