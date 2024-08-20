package com.fleetmate.trip.modules.nobilis.dto

import kotlinx.serialization.Serializable

@Serializable
data class NobilisPersonResponse (
    val patient_fio: String,
    val patient_birthday: String,
    val kindofexam_type: String,
    val result: String
)