package com.fleetmate.trip.modules.bumerang.service

import com.fleetmate.lib.utils.client.KtorClient
import com.fleetmate.trip.conf.ServerConf
import com.fleetmate.trip.modules.bumerang.dto.BumerangAuthResponse
import com.fleetmate.trip.modules.bumerang.dto.BumerangGetCarsResponse
import com.fleetmate.trip.modules.bumerang.dto.BumerangSingleCarResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CompletableDeferred
import org.kodein.di.DI

class BumerangService(di: DI) : KtorClient(di) {
    override val baseUrl: String = "https://bumerang-sat.ru"
    private var token: String = "Basic "

    private suspend fun auth() {
        token += sendRequest {
            method = HttpMethod.Post
            url {
                appendPathSegments(
                    "restapi", "users", "login"
                )
            }
            header("Content-Type", "application/json")
            header("Origin", "https://bumerang-sat.ru")
            header("Cookie", "JSESSIONID=1ae569ff19d5c1140f73d869a05d")
            setBody(ServerConf.bumerangCredentials)
        }.await().body<BumerangAuthResponse>().basic
    }

    private fun getCars(): CompletableDeferred<HttpResponse> =
        sendRequest {
            method = HttpMethod.Post
            url {
                appendPathSegments(
                    "restapi", "objects", "objects-tree-set"
                )
            }
            header("Content-Type", "application/json")
            header("Origin", "https://bumerang-sat.ru")
            header("Cookie", "JSESSIONID=1ae569ff19d5c1140f73d869a05d")
            header("Authorization", token)
            setBody(mapOf("groupType" to 0, "sort" to "name"))
        }

    suspend fun getCarCurrentSpeed(registrationNumber: String): Double {
        auth()
        val cars = (getCars().await().body<List<BumerangGetCarsResponse>>().firstOrNull() ?: return 0.0).objs
        val requestedCarId = cars.filter {
            it.licenseNumber == registrationNumber
        }
        if (requestedCarId.isEmpty()) return 0.0
        return (sendRequest {
            method = HttpMethod.Post
            url {
                appendPathSegments(
                    "restapi", "objects", "obj-details"
                )
            }
            header("Content-Type", "application/json")
            header("Origin", "https://bumerang-sat.ru")
            header("Cookie", "JSESSIONID=1ae569ff19d5c1140f73d869a05d")
            header("Authorization", token)
            setBody(mapOf(
                "objectId" to listOf(requestedCarId),
                "tz" to 3,
                "callDevice" to false,
            ))
        }.await().body<List<BumerangSingleCarResponse>>().firstOrNull() ?: return 0.0).speed
    }
}