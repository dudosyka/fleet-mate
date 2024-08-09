package com.fleetmate.trip.modules.division.service

import com.fleetmate.lib.dto.division.DivisionCreateDto
import com.fleetmate.lib.dto.division.DivisionOutputDto
import com.fleetmate.lib.model.division.DivisionModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI
import kotlin.collections.map

class DivisionService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): DivisionOutputDto? {
        return DivisionOutputDto(DivisionModel.getOne(id) ?: return null)
    }

    fun getAll(): List<DivisionOutputDto> {
        return DivisionModel.getAll().map {
            DivisionOutputDto(it)
        }
    }

    fun create(divisionCreateDto: DivisionCreateDto): DivisionOutputDto =
        DivisionOutputDto(DivisionModel.create(divisionCreateDto))

    fun update(id: Int, divisionUpdateDto: DivisionCreateDto): Boolean =
        DivisionModel.update(id, divisionUpdateDto)

    fun delete(id: Int): Boolean =
        DivisionModel.delete(id)
}