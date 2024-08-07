package com.fleetmate.trip.modules.automobile.service

import com.fleetmate.lib.dto.automobile.AutomobileCreateDto
import com.fleetmate.lib.dto.automobile.AutomobileOutputDto
import com.fleetmate.lib.dto.automobile.AutomobileUpdateDto
import com.fleetmate.lib.model.automobile.AutomobileModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI
import kotlin.collections.map

class AutomobileService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): AutomobileOutputDto? {
        return AutomobileOutputDto(AutomobileModel.getOne(id) ?: return null)
    }

    fun getAll(): List<AutomobileOutputDto> {
        return AutomobileModel.getAll().map {
            AutomobileOutputDto(it)
        }
    }

    fun create(automobileCreateDto: AutomobileCreateDto): AutomobileOutputDto =
        AutomobileOutputDto(AutomobileModel.create(automobileCreateDto))

    fun update(id: Int, automobileUpdateDto: AutomobileUpdateDto): Boolean =
        AutomobileModel.update(id, automobileUpdateDto)

    fun delete(id: Int): Boolean =
        AutomobileModel.delete(id)
}