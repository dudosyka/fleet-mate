package com.fleetmate.trip.modules.violation.service

import com.fleetmate.lib.conf.AppConf
import com.fleetmate.lib.data.dto.trip.TripOutputDto
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.lib.data.dto.violation.ViolationCreateDto
import com.fleetmate.lib.data.dto.violation.ViolationOutputDto
import com.fleetmate.lib.data.model.violation.ViolationModel
import org.kodein.di.DI
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.collections.map

class ViolationService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): ViolationOutputDto? {
        return ViolationOutputDto(ViolationModel.getOne(id) ?: return null)
    }

    fun getAll(): List<ViolationOutputDto> {
        return ViolationModel.getAll().map {
            ViolationOutputDto(it)
        }
    }

    private fun create(violationCreateDto: ViolationCreateDto): ViolationOutputDto =
        ViolationOutputDto(ViolationModel.create(violationCreateDto))

    fun createNoWash(tripDto: TripOutputDto): ViolationOutputDto =
        create(
            ViolationCreateDto(
                date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                type = AppConf.ViolationType.DEFAULT,
                duration = (0).toDouble(),
                hidden = false,
                driver = tripDto.driver,
                car = tripDto.car,
                comment = "There was no car wash",
                trip = tripDto.id
            )
        )
}