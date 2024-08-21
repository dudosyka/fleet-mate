package com.fleetmate.lib.plugins

import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders

val client = HttpClient()
suspend fun auth() {
    val response: HttpResponse = client.post("https://bumerang-sat.ru/restapi/users/login/") {
        headers {
            append(HttpHeaders.Origin, "https://bumerang-sat.ru")
            append(HttpHeaders.ContentType, "application/json")
            append(HttpHeaders.Cookie, "JSESSIONID=1ae569ff19d5c1140f73d869a05d")
        }
        setBody {
            """{"login": "ROD", "password": "20172253"}"""
        }
    }
}
suspend fun getAll(token: String) {
    val response: HttpResponse = client.post("https://bumerang-sat.ru/restapi/objects/objects-tree-set/") {
        headers {
            append(HttpHeaders.Origin, "https://bumerang-sat.ru")
            append(HttpHeaders.ContentType, "application/json")
            append(HttpHeaders.Cookie, "JSESSIONID=1ae569ff19d5c1140f73d869a05d")
            append(HttpHeaders.Authorization, "Basic $token")
        }
        setBody{
            """{"groupType": 0, "sort": "name"}"""
        }
    }
}
suspend fun getCar(token: String, automobileId: Int) {
    val response: HttpResponse = client.post("https://bumerang-sat.ru/restapi/objects/obj-details/") {
        headers {
            append(HttpHeaders.Origin, "https://bumerang-sat.ru")
            append(HttpHeaders.ContentType, "application/json")
            append(HttpHeaders.Cookie, "JSESSIONID=1ae569ff19d5c1140f73d869a05d")
            append(HttpHeaders.Authorization, "Basic $token")
        }
        setBody {
            """{"objectId": [$automobileId], "tz": 3, "callDevice": false}"""
        }
    }
}
