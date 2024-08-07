package com.fleetmate.trip.modules.check.service

import com.fleetmate.lib.dto.check.CheckCreateDto
import com.fleetmate.lib.dto.check.CheckOutputDto
import com.fleetmate.lib.dto.check.CheckUpdateDto
import com.fleetmate.lib.model.check.CheckModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI
import kotlin.collections.map

class CheckService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): CheckOutputDto? {
        return CheckOutputDto(CheckModel.getOne(id) ?: return null)
    }

    fun getAll(): List<CheckOutputDto> {
        return CheckModel.getAll().map {
            CheckOutputDto(it)
        }
    }

    fun create(checkCreateDto: CheckCreateDto): CheckOutputDto =
        CheckOutputDto(CheckModel.create(checkCreateDto))

    fun update(id: Int, checkUpdateDto: CheckUpdateDto): Boolean =
        CheckModel.update(id, checkUpdateDto)

    fun delete(id: Int): Boolean =
        CheckModel.delete(id)
}