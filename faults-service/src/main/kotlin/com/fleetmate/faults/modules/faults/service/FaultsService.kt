package com.fleetmate.faults.modules.faults.service

import com.fleetmate.faults.modules.faults.data.dto.FaultsCreateDto
import com.fleetmate.faults.modules.faults.data.dto.FaultsFullOutputDto
import com.fleetmate.faults.modules.faults.data.dto.FaultsUpdateDto
import com.fleetmate.faults.modules.faults.data.model.FaultsModel
import com.fleetmate.lib.data.dto.faults.FaultDto
import com.fleetmate.lib.dto.auth.AuthorizedUser
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI

class FaultsService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): FaultsFullOutputDto? {
        return FaultsFullOutputDto(FaultsModel.getOne(id) ?: return null)
    }

    fun getAll(): List<FaultsFullOutputDto> =
        FaultsModel.getAll().map { FaultsFullOutputDto(it) }

    fun create(authorizedUser: AuthorizedUser, faultsCreateDto: FaultsCreateDto): FaultsFullOutputDto =
        FaultsFullOutputDto(FaultsModel.create(authorizedUser, faultsCreateDto))

    fun update(id: Int, faultsUpdateDto: FaultsUpdateDto): Boolean =
        FaultsModel.update(id, faultsUpdateDto)

    fun delete(id: Int): Boolean =
        FaultsModel.delete(id)

    fun getAllCriticalByAutomobile(automobileId: Int): List<FaultDto> {
        val rows = FaultsModel.getAllCriticalByAutomobile(automobileId)
        val returnList = mutableListOf<FaultDto>()
        rows.forEach{
            returnList.add(
                FaultDto(
                    it
                )
            )
        }
        return returnList
    }
}