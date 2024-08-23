package com.fleetmate.trip.modules.nobilis.dto

import kotlinx.serialization.Serializable

@Serializable
data class NobilisResponse(
    val rows: List<NobilisPersonResponse>
)

@Serializable
data class NobilisPersonResponse (
    val patient_fio: String,
    val patient_birthday: Int,
    val kindofexam_type: String,
    val result: String
)