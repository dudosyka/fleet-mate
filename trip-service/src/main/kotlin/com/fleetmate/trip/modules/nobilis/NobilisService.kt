package com.fleetmate.trip.modules.nobilis

import com.fleetmate.lib.utils.client.KtorClient
import com.fleetmate.trip.conf.ServerConf
import com.fleetmate.trip.modules.nobilis.dto.NobilisAuthResponse
import com.fleetmate.trip.modules.nobilis.dto.NobilisPersonResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.time.OffsetDateTime
import java.time.ZoneId

class NobilisService: KtorClient() {
    override val baseUrl: String = "https://app.touchmed.ru"
    private var token: String = ""

    private suspend fun auth() {
        token = sendRequest {
            method = HttpMethod.Post
            url {
                appendPathSegments("login")
            }
            header("Content-Type", "application/json")
            setBody(ServerConf.nobilisCredentials)
        }.await().body<NobilisAuthResponse>().id_token
    }

    /**
     * birthday must be formatted as YYYYMMDD
     */
    suspend fun checkupExists(fullName: String, birthday: String): Boolean {
        auth()
        val filter = OffsetDateTime.now(ZoneId.systemDefault()).toString()
        return sendRequest {
            method = HttpMethod.Post
            url {
                appendPathSegments(
                    "report", "db", "report", "byJournalCheckup", "get"
                )
            }
            header("Content-Type", "application/json")
            setBody(mapOf(
                "paging" to mapOf(
                    "show" to 10,
                    "current" to 0,
                    "all" to 1
                ),
                "begin" to filter,
                "end" to filter,
                "performer" to mapOf<String, String>(),
                "device" to mapOf<String, String>(),
                "state" to listOf("done"),
                "uuidCompany" to "",
                "uuidPatientDivision" to listOf(
                    "37c2ad40-a862-11ed-9d9a-5f6995729dda"
                ),
                "patientcompany" to mapOf<String, String>(),
                "patientdivision" to mapOf<String, String>(),
                "patient" to mapOf(
                    "u4c" to mapOf(
                        "order" to listOf(listOf("number", "ASC"))
                    ),
                ),
                "name" to "",
                "sourcecompany" to mapOf<String, String>(),
                "kindofexam" to mapOf<String, String>(),
                "uuidKindofexam" to listOf(
                    "37cc9850-a862-11ed-9d9a-5f6995729dda",
                    "37cc4a30-a862-11ed-9d9a-5f6995729dda"
                ),
                "uuidSourceCompany" to "37c2ad40-a862-11ed-9d9a-5f6995729dda",
                "result" to listOf(
                    "allowed",
                    "repeat",
                    "forbidden",
                    "ignor",
                    "none"
                ),
                "resultInNull" to false,
                "token" to token
            ))
        }.await().body<List<NobilisPersonResponse>>().filter {
            it.result == "allowed" && it.patient_fio.lowercase() == fullName.lowercase() && it.patient_birthday == birthday && it.kindofexam_type == "checkup"
        }.isNotEmpty()
    }
}