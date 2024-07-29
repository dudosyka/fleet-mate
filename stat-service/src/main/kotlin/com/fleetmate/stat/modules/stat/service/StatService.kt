package com.fleetmate.stat.modules.stat.service

import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.stat.data.dto.StatCreateDto
import com.fleetmate.stat.modules.stat.data.dto.StatDto
import com.fleetmate.stat.modules.stat.data.dto.StatUpdateDto
import com.fleetmate.stat.modules.stat.data.model.StatModel
import org.kodein.di.DI

class StatService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): StatDto? {
        return StatDto(StatModel.getOne(id) ?: return null)
    }

    fun getAll(): List<StatDto> =
        StatModel.getAll().map { StatDto(it) }

    fun create(statCreateDto: StatCreateDto): StatDto =
        StatDto(StatModel.create(statCreateDto))

    fun update(id: Int, statUpdateDto: StatUpdateDto): Boolean =
        StatModel.update(id, statUpdateDto)

    fun delete(id: Int): Boolean =
        StatModel.delete(id)
}