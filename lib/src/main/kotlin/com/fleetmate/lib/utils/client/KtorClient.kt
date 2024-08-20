package com.fleetmate.lib.utils.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.actor
import kotlinx.serialization.json.Json

abstract class KtorClient {
    protected abstract val baseUrl: String
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    protected open class ClientRequest(val builder: HttpRequestBuilder.() -> Unit, var response: CompletableDeferred<HttpResponse>)

    @OptIn(ObsoleteCoroutinesApi::class)
    protected val requestActor = CoroutineScope(Job()).actor<ClientRequest> {
        for (event in this) {
            event.response.complete(client.request(baseUrl, event.builder))
        }
    }

    protected fun sendRequest(builder: HttpRequestBuilder.() -> Unit): CompletableDeferred<HttpResponse> {
        val response = CompletableDeferred<HttpResponse>()
        requestActor.trySend(ClientRequest(builder, response))
        return response
    }
}