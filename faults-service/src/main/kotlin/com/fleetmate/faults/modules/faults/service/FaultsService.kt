package com.fleetmate.faults.modules.faults.service

import com.fleetmate.faults.modules.faults.data.dto.FaultsCreateDto
import com.fleetmate.faults.modules.faults.data.dto.FaultsDto
import com.fleetmate.faults.modules.faults.data.dto.FaultsUpdateDto
import com.fleetmate.faults.modules.faults.data.model.FaultsModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI

class FaultsService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): FaultsDto? {
        return FaultsDto(FaultsModel.getOne(id) ?: return null)
    }

    fun getAll(): List<FaultsDto> =
        FaultsModel.getAll().map { FaultsDto(it) }

    fun create(faultsCreateDto: FaultsCreateDto): FaultsDto =
        FaultsDto(FaultsModel.create(faultsCreateDto))

    fun update(id: Int, faultsUpdateDto: FaultsUpdateDto): Boolean =
        FaultsModel.update(id, faultsUpdateDto)

    fun delete(id: Int): Boolean =
        FaultsModel.delete(id)
}