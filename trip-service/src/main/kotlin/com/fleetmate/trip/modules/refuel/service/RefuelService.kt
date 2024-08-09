package com.fleetmate.trip.modules.refuel.service

import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.refuel.data.dto.RefuelCreateDto
import com.fleetmate.trip.modules.refuel.data.dto.RefuelOutputDto
import com.fleetmate.trip.modules.refuel.data.dto.RefuelUpdateDto
import com.fleetmate.trip.modules.refuel.data.model.RefuelModel
import org.kodein.di.DI
import kotlin.collections.map

class RefuelService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): RefuelOutputDto? {
        return RefuelOutputDto(RefuelModel.getOne(id) ?: return null)
    }

    fun getAll(): List<RefuelOutputDto> {
        return RefuelModel.getAll().map {
            RefuelOutputDto(it)
        }
    }

    fun create(refuelCreateDto: RefuelCreateDto): RefuelOutputDto =
        RefuelOutputDto(RefuelModel.create(refuelCreateDto))

    fun update(id: Int, refuelUpdateDto: RefuelUpdateDto): Boolean =
        RefuelModel.update(id, refuelUpdateDto)

    fun delete(id: Int): Boolean =
        RefuelModel.delete(id)
}