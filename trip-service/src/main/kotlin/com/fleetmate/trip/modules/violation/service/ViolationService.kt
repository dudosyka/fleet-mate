package com.fleetmate.trip.modules.violation.service

import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.lib.data.dto.violation.ViolationCreateDto
import com.fleetmate.lib.data.dto.violation.ViolationOutputDto
import com.fleetmate.lib.data.dto.violation.ViolationUpdateDto
import com.fleetmate.trip.modules.violation.data.model.ViolationModel
import org.kodein.di.DI
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

    fun create(violationCreateDto: ViolationCreateDto): ViolationOutputDto =
        ViolationOutputDto(ViolationModel.create(violationCreateDto))

    fun update(id: Int, violationUpdateDto: ViolationUpdateDto): Boolean =
        ViolationModel.update(id, violationUpdateDto)

    fun delete(id: Int): Boolean =
        ViolationModel.delete(id)
}