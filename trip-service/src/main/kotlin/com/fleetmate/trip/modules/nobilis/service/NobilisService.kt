package com.fleetmate.trip.modules.nobilis.service

import com.fleetmate.lib.plugins.Logger
import com.fleetmate.lib.utils.client.KtorClient
import com.fleetmate.trip.conf.ServerConf
import com.fleetmate.trip.modules.nobilis.dto.NobilisAuthResponse
import com.fleetmate.trip.modules.nobilis.dto.NobilisPersonResponse
import com.fleetmate.trip.modules.nobilis.dto.NobilisResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import org.kodein.di.DI
import java.time.OffsetDateTime
import java.time.ZoneId

class NobilisService(di: DI) : KtorClient(di) {
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
    suspend fun checkupExists(fullName: String, birthday: Long): Boolean {
        auth()
        val filter = OffsetDateTime.now(ZoneId.systemDefault()).toString()
        //TODO: REMOVE IN PROD!!
        return true
//        sendRequest {
//            method = HttpMethod.Post
//            url {
//                appendPathSegments(
//                    "report", "db", "report", "byJournalCheckup", "get"
//                )
//            }
//            header("Content-Type", "application/json")
//            setBody("{\"paging\":{\"show\":10,\"current\":0,\"all\":1},\"begin\":\"${filter}\",\"end\":\"${filter}\",\"performer\":{},\"device\":{},\"state\":[\"done\"],\"uuidCompany\":\"\",\"uuidPatientDivision\":[\"37c2ad40-a862-11ed-9d9a-5f6995729dda\"],\"patientcompany\":{},\"patientdivision\":{},\"patient\":{\"u4c\":{\"order\":[[\"number\",\"ASC\"]]},\"name\":\"\"},\"sourcecompany\":{},\"kindofexam\":{},\"uuidKindofexam\":[\"37cc9850-a862-11ed-9d9a-5f6995729dda\",\"37cc4a30-a862-11ed-9d9a-5f6995729dda\"],\"uuidSourceCompany\":\"37c2ad40-a862-11ed-9d9a-5f6995729dda\",\"result\":[\"allowed\",\"repeat\",\"forbidden\",\"ignor\",\"none\"],\"resultInNull\":false,\"token\":\"${token}\"}")
//        }.await().body<NobilisResponse>().rows.any {
//            it.result == "allowed" && it.patient_fio.lowercase() == fullName.lowercase() && it.patient_birthday == birthday && it.kindofexam_type == "checkup"
//        }
    }
}