package com.fleetmate.trip.modules.position.service

import com.fleetmate.lib.data.dto.position.PositionCreateDto
import com.fleetmate.lib.data.dto.position.PositionOutputDto
import com.fleetmate.lib.data.model.position.PositionModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI
import kotlin.collections.map

class PositionService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): PositionOutputDto? {
        return PositionOutputDto(PositionModel.getOne(id) ?: return null)
    }

    fun getAll(): List<PositionOutputDto> {
        return PositionModel.getAll().map {
            PositionOutputDto(it)
        }
    }

    fun create(positionCreateDto: PositionCreateDto): PositionOutputDto =
        PositionOutputDto(PositionModel.create(positionCreateDto))
}